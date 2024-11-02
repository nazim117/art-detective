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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController, viewModel: ArtViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // Observe saved artworks from the ViewModel
    val savedArtworks by viewModel.savedArtworks.collectAsState()

    // Trigger fetching saved artworks when the composable is first loaded
    LaunchedEffect(Unit) {
        viewModel.fetchSavedArtworks()
    }

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

        if (savedArtworks.isNotEmpty()) {
            ArtGrid(
                artObjects = savedArtworks,
                onInfoClick = { artwork ->
                    selectedArtwork = artwork
                }
            )
        } else {
            Text("No saved artworks found.", modifier = Modifier.padding(16.dp))
        }
    }

    // Display info dialog when an artwork is selected
    selectedArtwork?.let { artwork ->
        InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
    }
}