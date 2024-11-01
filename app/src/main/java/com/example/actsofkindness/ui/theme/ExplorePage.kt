package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.actsofkindness.ArtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(navController: NavController, viewModel: ArtViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val categories by viewModel.categories.collectAsState()
    val artists by viewModel.artists.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRandomCategories()
        viewModel.fetchPopularArtists()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text("Explore") })

        Text("Categories", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        CategoryGrid(categories = categories, navController = navController)

        Text("Popular Artists", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        ArtistGrid(artists = artists, navController = navController)
    }
}