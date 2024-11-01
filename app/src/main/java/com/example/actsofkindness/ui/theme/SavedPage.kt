package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.ArtViewModel
import com.example.actsofkindness.WebImage

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController, viewModel: ArtViewModel) {
    val savedArtworks by viewModel.savedArtworks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedArtwork by remember { mutableStateOf<ArtObject?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text("Your Saved Artwork") })
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // Show loading indicator while data is being fetched
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator() // Spinner or any other loading indicator
            }
        } else {
            Text("Your Saved Art", style = MaterialTheme.typography.bodyLarge)

            if (savedArtworks.isNotEmpty()) {
                ArtGrid(
                    artObjects = savedArtworks,
                    viewModel = viewModel,
                    onInfoClick = { artwork -> selectedArtwork = artwork },
                    onSaveClick = { artwork -> viewModel.toggleSaveArtwork(artwork) }
                )
            } else {
                Text("No saved artworks found.", modifier = Modifier.padding(16.dp))
            }
        }
    }

    selectedArtwork?.let { artwork ->
        InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
    }
}
