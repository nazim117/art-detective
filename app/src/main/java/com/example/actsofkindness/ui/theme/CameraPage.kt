package com.example.actsofkindness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CameraPage(navController: NavController) {
    val context = LocalContext.current
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var previewView: PreviewView? = null
    var analysisResult by remember { mutableStateOf<String?>(null) }

    // Retrieve the API key from assets
    val apiKey = ""

    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView?.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        try {
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraX", "Failed to bind camera use cases", e)
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewView = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                if (apiKey != null) {
                    takePhoto(context, imageCapture) { bitmap ->
                        analyzeImageWithVisionApi(bitmap, apiKey!!) { result ->
                            analysisResult = result
                        }
                    }
                } else {
                    Log.e("CameraPage", "API Key is missing")
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text("Capture")
        }
    }

    analysisResult?.let {
        AlertDialog(
            onDismissRequest = { analysisResult = null },
            title = { Text("Analysis Result") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = { analysisResult = null }) { Text("OK") }
            }
        )
    }
}

// `takePhoto` function within CameraPage.kt
fun takePhoto(context: Context, imageCapture: ImageCapture?, onImageCaptured: (Bitmap) -> Unit) {
    if (imageCapture == null) {
        Log.e("CameraX", "ImageCapture is not initialized")
        return
    }

    val photoFile = File(
        context.filesDir,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    Log.d("CameraX", "Attempting to capture photo")

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("CameraX", "Photo capture succeeded: ${photoFile.absolutePath}")

                val uri = Uri.fromFile(photoFile)
                val bitmap = uriToBitmap(context, uri)
                if (bitmap != null) {
                    onImageCaptured(bitmap)
                } else {
                    Log.e("CameraX", "Failed to convert URI to Bitmap")
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
            }
        }
    )
}

// Helper function to convert URI to Bitmap
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also { inputStream?.close() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun analyzeImageWithVisionApi(bitmap: Bitmap, apiKey: String, onResult: (String) -> Unit) {
    val base64Image = bitmapToBase64(bitmap)

    val requestBody = VisionRequestBody(
        requests = listOf(
            VisionRequest(
                image = Image(content = base64Image),
                features = listOf(Feature(type = "WEB_DETECTION", maxResults = 10))
            )
        )
    )

    RetrofitClient.instance.analyzeImage(requestBody, apiKey).enqueue(object : Callback<VisionResponse> {
        override fun onResponse(call: Call<VisionResponse>, response: Response<VisionResponse>) {
            if (response.isSuccessful) {
                val bestGuess = response.body()?.responses?.firstOrNull()?.webDetection?.bestGuessLabels?.joinToString { it.label.orEmpty() }
                val entities = response.body()?.responses?.firstOrNull()?.webDetection?.webEntities?.joinToString { it.description.orEmpty() }
                onResult("Best Guess: $bestGuess\nEntities: $entities")
            } else {
                onResult("Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<VisionResponse>, t: Throwable) {
            onResult("Failure: ${t.message}")
        }
    })
}

