package com.example.uchan.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun submitReply(
        boardId: String,
        threadId: String?,
        name: String,
        comment: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val formBody = FormBody.Builder()
                .add("mode", "regist")
                .add("resto", threadId ?: "0")
                .add("name", name)
                .add("com", comment)
                .add("pwd", "")  // Password field (can be empty)
                .add("recaptcha_response", "") // Would need to implement captcha
                .add("email", "") // Email field (can be empty)
                .add("spoiler", "0")
                .add("json", "1") // Request JSON response
                .build()

            val request = Request.Builder()
                .url("https://sys.4chan.org/$boardId/post")
                .header("Referer", "https://boards.4chan.org/$boardId/")
                .header("Origin", "https://boards.4chan.org")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .post(formBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Failed to submit reply: ${response.message}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 