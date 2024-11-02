package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.ArtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(category: String, navController: NavController, viewModel: ArtViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val artObjects by viewModel.artObjects.collectAsState()
    var selectedArtwork by remember { mutableStateOf<ArtObject?>(null) } // For overlay info

    LaunchedEffect(category) {
        viewModel.fetchArtworks(category)
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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (artObjects.isNotEmpty()) {
                ArtGrid(artObjects = artObjects, viewModel = viewModel, onInfoClick = { artwork ->
                    selectedArtwork = artwork
                })
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
fun ArtworkCard(artwork: ArtObject, viewModel: ArtViewModel?, onInfoClick: (ArtObject) -> Unit) {
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
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
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
                IconButton(onClick = { viewModel?.saveArtwork(artwork) }) { // Only save if viewModel is not null
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Save",
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