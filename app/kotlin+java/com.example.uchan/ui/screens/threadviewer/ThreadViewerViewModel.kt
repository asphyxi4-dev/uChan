package com.example.uchan.ui.screens.threadviewer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchan.data.model.Post
import com.example.uchan.data.repository.PostRepository
import com.example.uchan.util.HtmlParser
import com.example.uchan.util.JsoupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.safety.Safelist
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ThreadViewerViewModel(application: Application) : AndroidViewModel(application) {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val postRepository = PostRepository()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    fun loadThread(boardId: String, threadId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _error.value = null
                
                val url = "https://boards.4chan.org/$boardId/thread/$threadId"
                when (val result = JsoupUtil.fetchDocument(url)) {
                    is JsoupUtil.Result.Success<*> -> {
                        val document = result.data as? Document
                        if (document != null) {
                            // Get all posts in the thread (OP and replies)
                            val posts = document.select(".post")
                                .mapNotNull { postElement ->
                                    val isOp = postElement.hasClass("op")
                                    parsePost(postElement, isOp)
                                }
                            
                            _posts.value = posts
                        } else {
                            _error.value = "Invalid document format"
                        }
                    }
                    is JsoupUtil.Result.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to load thread: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parsePost(element: Element, isThread: Boolean): Post {
        val postInfo = element.select(".postInfo").first()
        val fileText = element.select(".fileText").first()
        val postMessage = element.select(".postMessage").first()

        val imageUrl = fileText?.select("a")?.attr("href")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }
        val thumbnailUrl = element.select(".fileThumb img")?.attr("src")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }

        val postId = postInfo?.select(".postNum span")?.last()?.text()?.trim() ?: ""

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

    fun submitReply(name: String, comment: String, threadId: String, boardId: String) {
        viewModelScope.launch {
            try {
                _isSubmitting.value = true
                postRepository.submitReply(boardId, threadId, name, comment).fold(
                    onSuccess = {
                        // Refresh the thread after successful reply
                        loadThread(boardId, threadId)
                    },
                    onFailure = { exception ->
                        _error.value = "Failed to submit reply: ${exception.message}"
                    }
                )
            } finally {
                _isSubmitting.value = false
            }
        }
    }
} 