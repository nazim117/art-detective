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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController, viewModel: ArtViewModel) {
    val savedArtworks by viewModel.savedArtworks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedArtwork by remember { mutableStateOf<ArtObjectAPI?>(null) }

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            kotlinx.coroutines.delay(300)
            isRefreshing = false
        } else {
            isRefreshing = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(title = { Text("Your Saved Artwork") })

        Spacer(modifier = Modifier.height(24.dp))

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.fetchSavedArtworks() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text("Your Saved Art", style = MaterialTheme.typography.bodyLarge)

                    if (savedArtworks.isNotEmpty()) {
                        ArtGrid(
                            artObjectAPIS = savedArtworks,
                            viewModel = viewModel,
                            onInfoClick = { artwork -> selectedArtwork = artwork },
                            onSaveClick = { artwork -> viewModel.toggleSaveArtwork(artwork) }
                        )
                    } else {
                        Text("No saved artworks found.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    selectedArtwork?.let { artwork ->
        InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
    }
}