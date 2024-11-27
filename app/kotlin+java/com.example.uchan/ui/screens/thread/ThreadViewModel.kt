package com.example.uchan.ui.screens.thread

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchan.data.model.Post
import com.example.uchan.util.HtmlParser
import com.example.uchan.util.JsoupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import com.example.uchan.data.model.BookmarkEntity
import com.example.uchan.data.dao.BookmarkDao
import com.example.uchan.data.db.UChanDatabase

class ThreadViewModel(application: Application) : AndroidViewModel(application) {
    private val _threads = MutableStateFlow<List<Post>>(emptyList())
    val posts = _threads.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val client = OkHttpClient()

    private val bookmarkDao = UChanDatabase.getInstance(application).bookmarkDao()
    
    private val _isBookmarked = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isBookmarked = _isBookmarked.asStateFlow()

    fun loadThread(boardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                
                val url = "https://boards.4chan.org/$boardId/"
                when (val result = JsoupUtil.fetchDocument(url)) {
                    is JsoupUtil.Result.Success<*> -> {
                        val document = result.data as? Document
                        if (document != null) {
                            // Parse each thread directly from the document
                            val threads = document.select(".thread")
                                .mapNotNull { threadElement ->
                                    // Get the thread ID from the thread element
                                    val threadId = threadElement.attr("id").replace("t", "")
                                    // Get the OP post from each thread
                                    val op = threadElement.select(".post.op").first()
                                    op?.let { parsePost(it, true, threadId) }
                                }
                            
                            _threads.value = threads

                            // Check bookmark status for each thread
                            val bookmarkMap = mutableMapOf<String, Boolean>()
                            threads.forEach { post ->
                                val threadUrl = "https://boards.4chan.org/$boardId/thread/${post.postId}"
                                val isBookmarked = bookmarkDao.isBookmarked(threadUrl)
                                bookmarkMap[post.postId] = isBookmarked
                            }
                            _isBookmarked.value = bookmarkMap
                        } else {
                            _error.value = "Invalid document format"
                        }
                    }
                    is JsoupUtil.Result.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to load threads: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parsePost(element: Element, isThread: Boolean, threadId: String? = null): Post {
        val postInfo = element.select(".postInfo").first()
        val fileText = element.select(".fileText").first()
        val postMessage = element.select(".postMessage").first()

        val imageUrl = fileText?.select("a")?.attr("href")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }
        val thumbnailUrl = element.select(".fileThumb img")?.attr("src")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }

        // Use the thread ID for thread posts, otherwise use the post number
        val postId = if (isThread && threadId != null) {
            threadId
        } else {
            postInfo?.select(".postNum span")?.last()?.text()?.trim()?.replace("No.", "") ?: ""
        }

        val cleanContent = postMessage?.let { message ->
            val content = message.html()
                .replace("<br>", "\n")
                .replace("<wbr>", "")
            
            Jsoup.clean(content, Safelist.none())
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .trim()
        } ?: ""

        val replies = element.select(".quotelink")
            .map { it.text().trim().replace(">>(\\d+)".toRegex(), "$1") }
            .filter { it.isNotEmpty() }

        return Post(
            postId = postId,
            author = postInfo?.select(".name")?.text()?.trim() ?: "Anonymous",
            timestamp = postInfo?.select(".dateTime")?.text()?.trim() ?: "",
            content = cleanContent,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            replies = replies,
            isThread = isThread
        )
    }

    fun createThread(
        boardId: String,
        name: String,
        subject: String,
        comment: String,
        captchaResponse: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val formData = FormBody.Builder()
                    .add("mode", "regist")
                    .add("name", name)
                    .add("sub", subject)
                    .add("com", comment)
                    .add("t-response", captchaResponse)
                    .build()

                val request = Request.Builder()
                    .url("https://sys.4chan.org/$boardId/post")
                    .post(formData)
                    .header("Referer", "https://boards.4chan.org/$boardId/")
                    .header("Origin", "https://boards.4chan.org")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        _error.value = "Failed to create thread: ${response.message}"
                    } else {
                        loadThread(boardId)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error creating thread: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleBookmark(post: Post, boardId: String) {
        viewModelScope.launch {
            val url = "https://boards.4chan.org/$boardId/thread/${post.postId}"
            val isCurrentlyBookmarked = _isBookmarked.value[post.postId] ?: false
            
            if (isCurrentlyBookmarked) {
                bookmarkDao.deleteBookmark(
                    BookmarkEntity(
                        url = url,
                        boardId = boardId,
                        threadId = post.postId,
                        title = post.content.take(50) // First 50 chars as title
                    )
                )
            } else {
                bookmarkDao.insertBookmark(
                    BookmarkEntity(
                        url = url,
                        boardId = boardId,
                        threadId = post.postId,
                        title = post.content.take(50)
                    )
                )
            }
            
            // Update bookmark state
            _isBookmarked.value = _isBookmarked.value.toMutableMap().apply {
                put(post.postId, !isCurrentlyBookmarked)
            }
        }
    }
} 