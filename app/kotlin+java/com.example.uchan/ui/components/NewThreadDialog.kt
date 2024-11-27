package com.example.uchan.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import coil.request.ImageRequest
import org.json.JSONObject

@Composable
fun NewThreadDialog(
    boardId: String,
    onDismiss: () -> Unit,
    onSubmit: (name: String, subject: String, comment: String, captchaResponse: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var captchaResponse by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var captchaChallenge by remember { mutableStateOf("") }
    var captchaId by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val client = remember { OkHttpClient() }

    // Function to get new captcha
    suspend fun getCaptcha() {
        try {
            val request = Request.Builder()
                .url("https://boards.4channel.org/${boardId}/captcha.php")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "*/*")
                .header("Referer", "https://boards.4channel.org/$boardId/")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    captchaId = System.currentTimeMillis().toString()
                    Log.d("Captcha", "Got new captcha ID: $captchaId")
                } else {
                    Log.e("Captcha", "Failed to get captcha: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e("Captcha", "Error getting captcha", e)
        }
    }

    LaunchedEffect(Unit) {
        getCaptcha()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Thread in /$boardId/") },
        text = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (optional)") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject (optional)") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comment") },
                    minLines = 3,
                    maxLines = 5
                )

                // Captcha section
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data("https://boards.4channel.org/${boardId}/captcha.php?timestamp=${System.currentTimeMillis()}")
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .addHeader("Referer", "https://boards.4channel.org/$boardId/")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Captcha image",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp),
                                onError = {
                                    Log.e("Captcha", "Failed to load image: ${it.result.throwable}")
                                },
                                onSuccess = {
                                    Log.d("Captcha", "Successfully loaded captcha image")
                                }
                            )
                            
                            IconButton(
                                onClick = { 
                                    scope.launch {
                                        getCaptcha()
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Refresh, "Refresh captcha")
                            }
                        }

                        OutlinedTextField(
                            value = captchaResponse,
                            onValueChange = { captchaResponse = it },
                            label = { Text("Enter captcha") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        comment.isBlank() -> {
                            errorMessage = "Comment cannot be empty"
                        }
                        captchaResponse.isBlank() -> {
                            errorMessage = "Please solve the captcha"
                        }
                        else -> {
                            onSubmit(name, subject, comment, "$captchaId.$captchaResponse")
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 