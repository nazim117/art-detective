package com.example.actsofkindness

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
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

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Adjust orientation based on EXIF data
        val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Extension function to rotate Bitmap if necessary
fun Bitmap.rotateIfNecessary(): Bitmap {
    return try {
        val matrix = Matrix().apply { postRotate(90f) } // Example with 90° rotation
        Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    } catch (e: Exception) {
        this // Return the original bitmap if rotation fails
    }
}

// Take a photo and return the bitmap, now retaining color and correct orientation
fun takePhoto(
    context: Context,
    imageCapture: ImageCapture?,
    onImageCaptured: (Bitmap, Bitmap) -> Unit // Now returns both color and grayscale bitmaps
) {
    if (imageCapture == null) {
        Log.e("CameraX", "ImageCapture is not initialized")
        return
    }

    val photoFile = File(
        context.filesDir,
        SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.US
        ).format(System.currentTimeMillis()) + ".jpg"
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
                val colorBitmap = uriToBitmap(context, uri)
                if (colorBitmap != null) {
                    val grayscaleBitmap = preprocessImage(colorBitmap) // Generate grayscale version
                    onImageCaptured(colorBitmap, grayscaleBitmap)
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

// Helper function to convert a Base64-encoded string to Bitmap
fun String.toBitmap(): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("CaptureCard", "Error decoding base64 to Bitmap: ${e.message}")
        null
    }
}
