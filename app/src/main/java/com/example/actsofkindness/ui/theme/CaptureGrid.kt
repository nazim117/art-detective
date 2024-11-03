package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.actsofkindness.CaptureObject
import com.example.actsofkindness.toBitmap

@Composable
fun CaptureGrid(
    captureObjects: List<CaptureObject>,
    onInfoClick: (CaptureObject) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(captureObjects) { capture ->
            CaptureCard(capture = capture, onInfoClick = onInfoClick)
        }
    }
}

@Composable
fun CaptureCard(capture: CaptureObject, onInfoClick: (CaptureObject) -> Unit) {
    val imageBitmap = capture.imageBase64.toBitmap()

    TextButton(
        onClick = { onInfoClick(capture) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = capture.description,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 2.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image Error", textAlign = TextAlign.Center)
                }
            }

            Text(
                text = capture.description.take(20) + "...",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(90.dp)
            )
        }
    }
}

@Composable
fun CaptureInfoDialog(capture: CaptureObject, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        },
        title = { Text("Capture Details") },
        text = {
            Column {
                Text("Description: ${capture.description}")
                Spacer(modifier = Modifier.height(8.dp))

                capture.imageBase64.toBitmap()?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = capture.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    )
}
