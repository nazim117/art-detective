package com.example.actsofkindness

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CameraPage(navController: NavController) {
    val context = LocalContext.current
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var previewView: PreviewView? = null
    var analysisResult by remember { mutableStateOf<String?>(null) }

    // Retrieve the API key from assets or define it here directly
    val apiKey = "API_KEY" // Replace with your actual API key

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
                if (apiKey.isNotEmpty()) {
                    takePhoto(context, imageCapture) { bitmap ->
                        analyzeImageWithVisionApi(bitmap, apiKey) { result ->
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
        ResultDialog(analysisResult = it, onDismiss = { analysisResult = null })
    }
}

// Display the analysis result in a dialog
@Composable
fun ResultDialog(analysisResult: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Analysis Result") },
        text = { Text(analysisResult) },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

// Function to analyze image using Vision API
fun analyzeImageWithVisionApi(bitmap: Bitmap, apiKey: String, onResult: (String) -> Unit) {
    val base64Image = bitmapToBase64(bitmap)

    val requestBody = VisionRequestBody(
        requests = listOf(
            VisionRequest(
                image = Image(content = base64Image),
                features = listOf(
                    Feature(type = "WEB_DETECTION", maxResults = 10),
                    Feature(type = "LABEL_DETECTION", maxResults = 10)
                )
            )
        )
    )

    RetrofitClient.instance.analyzeImage(requestBody, apiKey).enqueue(object : Callback<VisionResponse> {
        override fun onResponse(call: Call<VisionResponse>, response: Response<VisionResponse>) {
            if (response.isSuccessful) {
                val bestGuess = response.body()?.responses?.firstOrNull()?.webDetection?.bestGuessLabels
                    ?.filter { it.label != null }
                    ?.joinToString { it.label.orEmpty() }

                val webEntities = response.body()?.responses?.firstOrNull()?.webDetection?.webEntities
                    ?.filter { it.description != null && (it.score ?: 0f) >= 0.8 }
                    ?.joinToString { it.description.orEmpty() }

                val labelEntities = response.body()?.responses?.firstOrNull()?.labelAnnotations
                    ?.filter { it.description != null && (it.score ?: 0f) >= 0.8 }
                    ?.joinToString { it.description.orEmpty() }

                val combinedResult = buildString {
                    appendLine("Best Guess: $bestGuess")
                    appendLine("Web Entities: $webEntities")
                    appendLine("Label Entities: $labelEntities")
                }

                onResult(combinedResult)
            } else {
                onResult("Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<VisionResponse>, t: Throwable) {
            onResult("Failure: ${t.message}")
        }
    })
}
