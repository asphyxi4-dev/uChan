package com.example.uchan.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object JsoupUtil {
    private const val TIMEOUT_MS = 10000 // 10 seconds
    private const val USER_AGENT = "Mozilla/5.0 (Android) uChan/1.0"

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    suspend fun fetchDocument(url: String): Result<Document> {
        return try {
            val doc = Jsoup.connect(url)
                .timeout(TIMEOUT_MS)
                .userAgent(USER_AGENT)
                .get()
            Result.Success(doc)
        } catch (e: SocketTimeoutException) {
            Result.Error("Connection timed out")
        } catch (e: UnknownHostException) {
            Result.Error("No internet connection")
        } catch (e: IOException) {
            Result.Error("Failed to load page: ${e.message}")
        } catch (e: Exception) {
            Result.Error("An unexpected error occurred: ${e.message}")
        }
    }

    fun parseHtml(html: String): Result<Document> {
        return try {
            val doc = Jsoup.parse(html)
            Result.Success(doc)
        } catch (e: Exception) {
            Result.Error("Failed to parse HTML: ${e.message}")
        }
    }
} 