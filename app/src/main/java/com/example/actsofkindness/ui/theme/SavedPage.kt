package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actsofkindness.ArtObjectAPI
import com.example.actsofkindness.ArtViewModel
import com.example.actsofkindness.CaptureObject
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController, viewModel: ArtViewModel) {
    val savedArtworks by viewModel.savedArtworks.collectAsState()
    val savedCaptures by viewModel.savedCaptures.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedArtwork by remember { mutableStateOf<ArtObjectAPI?>(null) }
    var selectedCapture by remember { mutableStateOf<CaptureObject?>(null) }

    val isRefreshing = remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing.value)

    LaunchedEffect(Unit) {
        viewModel.fetchSavedArtworks()
        viewModel.fetchSavedCaptures()
    }

    LaunchedEffect(isLoading) {
        isRefreshing.value = isLoading
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Saved Items") })
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.fetchSavedArtworks()
                viewModel.fetchSavedCaptures()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Your Saved Artwork", style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    if (savedArtworks.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp, max = 400.dp)
                        ) {
                            ArtGrid(
                                artObjectAPIS = savedArtworks,
                                viewModel = viewModel,
                                onInfoClick = { artwork -> selectedArtwork = artwork },
                                onSaveClick = { artwork -> viewModel.toggleSaveArtwork(artwork) }
                            )
                        }
                    } else {
                        Text("No saved artworks found.", modifier = Modifier.padding(16.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text("Your Saved Captures", style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    if (savedCaptures.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp, max = 400.dp)
                        ) {
                            CaptureGrid(
                                captureObjects = savedCaptures,
                                onInfoClick = { capture -> selectedCapture = capture }
                            )
                        }
                    } else {
                        Text("No saved captures found.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    selectedArtwork?.let { artwork ->
        InfoDialog(artwork = artwork, onClose = { selectedArtwork = null })
    }
    selectedCapture?.let { capture ->
        CaptureInfoDialog(capture = capture, onClose = { selectedCapture = null })
    }
}
