package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import com.example.actsofkindness.ArtObjectAPI
import com.example.actsofkindness.ArtViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(
    category: String,
    navController: NavController,
    viewModel: ArtViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val artObjects by viewModel.artObjectsAPI.collectAsState()
    val showSaveSnackbar by viewModel.showSaveSnackbar.collectAsState()
    var selectedArtwork by remember { mutableStateOf<ArtObjectAPI?>(null) }

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
                    artObjectAPIS = artObjects,
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