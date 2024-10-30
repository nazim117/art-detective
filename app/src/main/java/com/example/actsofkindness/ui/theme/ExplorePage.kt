package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.actsofkindness.Artist
import com.example.actsofkindness.Category

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


@Composable
fun ArtistGrid(artists: List<Artist>, navController: NavController) {
    LazyRow(
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        items(artists.size) { index ->
            val artist = artists[index]
            TextButton(onClick = {
                navController.navigate("results/${artist.name}")
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    artist.imageUrl?.let { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = artist.name,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 4.dp)
                        )
                    }
                    Text(
                        artist.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun CategoryGrid(categories: List<Category>, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(categories.size) { index ->
                val category = categories[index]
                TextButton(onClick = {
                    navController.navigate("results/${category.name}")
                }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        category.imageUrl?.let { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = category.name,
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(bottom = 4.dp)
                            )
                        }
                        Text(category.name, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun ArtGrid(artObjects: List<ArtObject>, onInfoClick: (ArtObject) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(artObjects) { artwork ->
            ArtworkCard(artwork = artwork, onInfoClick = onInfoClick)
        }
    }
}