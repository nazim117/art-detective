package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.WebImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController) {
    val savedArtworks = listOf(
        ArtObject(
            id = "1",
            title = "Starry Night",
            principalOrFirstMaker = "Vincent van Gogh",
            webImage = WebImage(url = "https://example.com/starry_night.jpg")
        ),
        ArtObject(
            id = "2",
            title = "Mona Lisa",
            principalOrFirstMaker = "Leonardo da Vinci",
            webImage = WebImage(url = "https://example.com/mona_lisa.jpg")
        ),
        ArtObject(
            id = "3",
            title = "The Persistence of Memory",
            principalOrFirstMaker = "Salvador Dalí",
            webImage = WebImage(url = "https://example.com/persistence_of_memory.jpg")
        )
    )

    var selectedArtwork by remember { mutableStateOf<ArtObject?>(null) } // For overlay info

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Your Saved Artworks") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Saved Art", style = MaterialTheme.typography.bodyLarge)

        ArtGrid(
            artObjects = savedArtworks,
            onInfoClick = { artwork ->
                selectedArtwork = artwork
            }
        )
    }

    // Display info dialog when an artwork is selected
    selectedArtwork?.let { artwork ->
        InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
    }
}
