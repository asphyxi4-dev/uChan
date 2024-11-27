package com.example.uchan.util

import com.example.uchan.data.model.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.safety.Safelist

object HtmlParser {
    fun parseThread(html: String): JsoupUtil.Result<List<Post>> {
        return when (val result = JsoupUtil.parseHtml(html)) {
            is JsoupUtil.Result.Success -> {
                try {
                    val posts = result.data.select(".postContainer").map { element ->
                        parsePost(element)
                    }
                    JsoupUtil.Result.Success(posts)
                } catch (e: Exception) {
                    JsoupUtil.Result.Error("Failed to parse posts: ${e.message}")
                }
            }
            is JsoupUtil.Result.Error -> JsoupUtil.Result.Error(result.message)
        }
    }

    private fun parsePost(element: Element): Post {
        val postInfo = element.select(".postInfo").first()
        val fileText = element.select(".fileText").first()
        val postMessage = element.select(".postMessage").first()

        val imageUrl = fileText?.select("a")?.attr("href")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }
        val thumbnailUrl = element.select(".fileThumb img")?.attr("src")?.let { 
            if (it.startsWith("//")) "https:$it" else it 
        }

        // Get thread number (for thread posts)
        val threadNo = element.select(".thread")?.attr("id")?.replace("t", "") ?: ""

        // Clean and format the post content
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

        // Extract and clean replies
        val replies = element.select(".quotelink")
            .map { it.text().trim().replace(">>(\\d+)".toRegex(), "$1") }
            .filter { it.isNotEmpty() }

        return Post(
            postId = threadNo.ifEmpty { 
                postInfo?.select(".postNum span")?.last()?.text()?.trim() ?: "" 
            },
            author = postInfo?.select(".name")?.text()?.trim() ?: "Anonymous",
            timestamp = postInfo?.select(".dateTime")?.text()?.trim() ?: "",
            content = cleanContent,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            replies = replies,
            isThread = threadNo.isNotEmpty() // Only true for actual threads
        )
    }
} 