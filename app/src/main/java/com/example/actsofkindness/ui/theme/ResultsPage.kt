package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.ArtViewModel
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Favorite

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

@Composable
fun ArtworkCard(
    artwork: ArtObject,
    viewModel: ArtViewModel?,
    onInfoClick: (ArtObject) -> Unit,
    onSaveClick: (ArtObject) -> Unit
) {
    val savedArtworks by viewModel?.savedArtworks?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    val isSaved = savedArtworks.any { it.title == artwork.title }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            artwork.webImage?.url?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = artwork.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = artwork.title,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = artwork.principalOrFirstMaker,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onSaveClick(artwork) }) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isSaved) "Remove from saved" else "Save",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onInfoClick(artwork) }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}