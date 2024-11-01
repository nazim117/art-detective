package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.actsofkindness.Artist

@Composable
fun ArtistGrid(artists: List<Artist>, navController: NavController) {
    LazyRow(
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        items(artists.size) { index ->
            val artist = artists[index]
            TextButton(onClick = {
                navController.navigate("results/${artist.name}")
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    artist.imageUrl?.let { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = artist.name,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 4.dp)
                        )
                    }
                    Text(
                        artist.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }
    }
}