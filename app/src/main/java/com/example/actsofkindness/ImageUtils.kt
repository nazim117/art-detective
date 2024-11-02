package com.example.actsofkindness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

// Preprocess the image to improve recognition accuracy
fun preprocessImage(bitmap: Bitmap): Bitmap {
    val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(grayscaleBitmap)
    val paint = Paint()

    // Apply grayscale effect
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return grayscaleBitmap
}

// Convert bitmap to base64
fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

// Convert URI to Bitmap
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also { inputStream?.close() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Take a photo and return the bitmap
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
                    val processedBitmap = preprocessImage(bitmap)
                    onImageCaptured(processedBitmap)
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
