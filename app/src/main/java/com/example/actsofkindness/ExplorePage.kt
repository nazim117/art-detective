package com.example.actsofkindness

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(navController: NavController){
    var selectedInfo by remember { mutableStateOf<String?>(null) }

    var showOverlay by remember { mutableStateOf(false) }
    var overlayItems by remember { mutableStateOf(emptyList<String>())}

    val categories = listOf("Art 1", "Art 2", "Art 3", "Art 4")
    val popularArtists = listOf("Artist 1", "Artist 2", "Artist 3")

    Box(modifier = Modifier.fillMaxSize()){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            item{
                TopAppBar(
                    title = { Text("Explore")},
                )
            }

            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Categories", style = MaterialTheme.typography.bodyLarge)
                    TextButton(onClick = {
                        overlayItems = categories + categories
                        showOverlay = true
                    }) {
                        Text("View All")
                    }
                }
            }

            item{
                GridLayout(
                    items= categories,
                    onItemInfoClick = {info ->
                        selectedInfo = info
                    },
                    modifier = Modifier
                        .height(300.dp)
                )
            }

            item{
                Spacer(modifier = Modifier.height(16.dp))
            }

            item{
                Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Popular Artists", style = MaterialTheme.typography.bodyLarge)
                    TextButton(onClick = {
                        overlayItems = popularArtists + popularArtists
                        showOverlay = true
                    }) {
                        Text("View All")
                    }
                }
            }

            item{
                GridLayout(
                    items = popularArtists,
                    onItemInfoClick = {info ->
                        selectedInfo = info
                    },
                    modifier = Modifier
                        .height(200.dp)
                )
            }
        }

        if(showOverlay){
            OverlayGrid(
                items = overlayItems,
                onClose = { showOverlay = false}
            )
        }
    }
    selectedInfo?.let{
        InfoDialog(info = it, onClose = {selectedInfo = null})
    }
}