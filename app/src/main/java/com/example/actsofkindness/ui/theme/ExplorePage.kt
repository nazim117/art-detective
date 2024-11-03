package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item{
            TopAppBar(title = { Text("Explore") })
        }

        item{
            Text("Categories", style = MaterialTheme.typography.bodyLarge)
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            CategoryGrid(categories = categories, navController = navController)
        }

        item{
            Text("Popular Artists", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 16.dp))
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            ArtistGrid(artists = artists, navController = navController)
        }
    }
}