package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.ArtViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(category: String, navController: NavController, viewModel: ArtViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val artObjects by viewModel.artObjects.collectAsState()
    val showSaveSnackbar by viewModel.showSaveSnackbar.collectAsState()
    var selectedArtwork by remember { mutableStateOf<ArtObject?>(null) }

    LaunchedEffect(category) {
        viewModel.fetchArtworks(category)
    }

    if (showSaveSnackbar) {
        LaunchedEffect(showSaveSnackbar) {
            delay(3000)
            viewModel._showSaveSnackbar.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results for $category") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = {
            if (showSaveSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    content = { Text("Artwork saved") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (artObjects.isNotEmpty()) {
                ArtGrid(
                    artObjects = artObjects,
                    viewModel = viewModel,
                    onInfoClick = { artwork -> selectedArtwork = artwork },
                    onSaveClick = { artwork -> viewModel.toggleSaveArtwork(artwork) }
                )
            } else {
                Text("No artworks found for $category.", modifier = Modifier.padding(16.dp))
            }
        }

        selectedArtwork?.let { artwork ->
            InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
        }
    }
}