package com.example.actsofkindness

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(navController: NavController) {
    var selectedInfo by remember { mutableStateOf<String?>(null) }
    var showOverlay by remember { mutableStateOf(false) }
    var overlayItems by remember { mutableStateOf(emptyList<String>()) }

    val categories = listOf("Painting", "Sculpture", "Photography")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    val artPieces = when (selectedCategory) {
        "Painting" -> listOf("Art 1", "Art 2", "Art 3", "Art 4", "Art 5", "Art 6", "Art 7", "Art 8", "Art 9")
        "Sculpture" -> listOf("Art 10", "Art 11", "Art 12", "Art 13", "Art 14", "Art 15", "Art 16", "Art 17", "Art 18")
        "Photography" -> listOf("Art 19", "Art 20", "Art 21", "Art 22", "Art 23", "Art 24", "Art 25", "Art 26", "Art 27")
        else -> emptyList()
    }

    val displayedArtPieces = artPieces.take(6)

    val popularArtists = listOf("Artist 1", "Artist 2", "Artist 3")

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            overlayItems = artPieces
                            showOverlay = true
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "View More Art",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(categories.size) { index ->
                            val category = categories[index]
                            ElevatedButton(
                                onClick = { selectedCategory = category },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (selectedCategory == category)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    else
                                        MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .scale(if (selectedCategory == category) 1.1f else 1.0f),
                                elevation = ButtonDefaults.buttonElevation(4.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = if (selectedCategory == category)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                GridLayout(
                    items = displayedArtPieces,
                    onItemInfoClick = { info ->
                        selectedInfo = info
                    },
                    modifier = Modifier
                        .height(300.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Artists",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(
                        onClick = {
                            overlayItems = popularArtists + popularArtists
                            showOverlay = true
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "View More Artists",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                GridLayout(
                    items = popularArtists,
                    onItemInfoClick = { info ->
                        selectedInfo = info
                    },
                    modifier = Modifier
                        .height(200.dp)
                )
            }
        }

        if (showOverlay) {
            OverlayGrid(
                items = overlayItems.take(9),
                onClose = { showOverlay = false }
            )
        }
    }

    selectedInfo?.let {
        InfoDialog(info = it, onClose = { selectedInfo = null })
    }
}
