package com.example.uchan.ui.components

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.io.IOException
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ImageViewerDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showPermissionDialog by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }

    // Calculate background alpha based on zoom level
    val backgroundAlpha by remember(scale) {
        derivedStateOf { 
            if (scale > 1f) 0.3f else 0.9f
        }
    }

    // Function to get proper image URL with correct protocol
    val processedImageUrl = remember(imageUrl) {
        when {
            imageUrl.startsWith("//") -> "https:$imageUrl"
            !imageUrl.startsWith("http") -> "https://$imageUrl"
            else -> imageUrl
        }
    }

    // Function to determine if URL is a video/webm
    val isVideo = remember(processedImageUrl) {
        processedImageUrl.endsWith(".webm", ignoreCase = true)
    }

    // Add ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
        }
    }

    // Clean up ExoPlayer when dialog is dismissed
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Storage permission is required to download images.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Dialog(
        onDismissRequest = {
            exoPlayer.release()
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
        ) {
            if (!isVideo) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(processedImageUrl)
                        .crossfade(true)
                        .allowHardware(true)  // Enable hardware acceleration
                        .allowRgb565(true)    // Allow RGB565 format for better memory usage
                        .build(),
                    contentDescription = "Full size image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.5f, 3f)
                                offsetX += pan.x * scale
                                offsetY += pan.y * scale
                            }
                        },
                    contentScale = ContentScale.Fit,
                    onError = {
                        Log.e("ImageViewer", "Error loading image: ${it.result.throwable}")
                    }
                )
            } else {
                // WebM video player
                DisposableEffect(processedImageUrl) {
                    val mediaItem = MediaItem.fromUri(processedImageUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    
                    onDispose {
                        exoPlayer.stop()
                        exoPlayer.clearMediaItems()
                    }
                }

                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = true // Show playback controls
                            setShowNextButton(false)
                            setShowPreviousButton(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            if (hasStoragePermission(context)) {
                                isDownloading = true
                                try {
                                    if (isVideo) {
                                        downloadVideo(context, imageUrl)
                                    } else {
                                        downloadImage(context, imageUrl)
                                    }
                                    Toast.makeText(
                                        context,
                                        if (isVideo) "Video downloaded successfully" else "Image downloaded successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Failed to download: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isDownloading = false
                                }
                            } else {
                                showPermissionDialog = true
                            }
                        }
                    },
                    enabled = !isDownloading
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "Download",
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private fun hasStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        true // Android 10 and above don't need explicit permission for downloads
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private suspend fun downloadImage(context: Context, imageUrl: String) {
    withContext(Dispatchers.IO) {
        try {
            val processedUrl = when {
                imageUrl.startsWith("//") -> "https:$imageUrl"
                !imageUrl.startsWith("http") -> "https://$imageUrl"
                else -> imageUrl
            }

            // Determine file extension from URL or fallback to jpg
            val extension = processedUrl.substringAfterLast(".", "jpg")
            val fileName = "uchan_${System.currentTimeMillis()}.$extension"
            
            val mimeType = when (extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                else -> "image/jpeg"  // fallback
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/UChan")
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: throw IOException("Failed to create MediaStore entry")

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    URL(processedUrl).openStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val uchanDir = File(imagesDir, "UChan").apply { mkdirs() }
                val file = File(uchanDir, fileName)

                URL(processedUrl).openStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to download image: ${e.message}")
        }
    }
}

// Add new function for video downloads
private suspend fun downloadVideo(context: Context, videoUrl: String) {
    withContext(Dispatchers.IO) {
        try {
            val processedUrl = when {
                videoUrl.startsWith("//") -> "https:$videoUrl"
                !videoUrl.startsWith("http") -> "https://$videoUrl"
                else -> videoUrl
            }

            val fileName = "uchan_${System.currentTimeMillis()}.webm"
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "video/webm")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/UChan")
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: throw IOException("Failed to create MediaStore entry")

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    URL(processedUrl).openStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } else {
                val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                val uchanDir = File(moviesDir, "UChan").apply { mkdirs() }
                val file = File(uchanDir, fileName)

                URL(processedUrl).openStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to download video: ${e.message}")
        }
    }
} 