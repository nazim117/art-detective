package com.example.actsofkindness

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@Composable
fun OverlayGrid(items: List<String>, onClose: () -> Unit) {
    var selectedInfo by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false) // Fullscreen modal
    ) {
        // Main overlay content with animations
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClose) {
                        Text("Close", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Title for grid content
                Text(
                    text = "Explore More Items",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Grid Layout for more items
                GridLayout(
                    items = items,
                    onItemInfoClick = {info ->
                        selectedInfo = info
                    },
                    modifier = Modifier.fillMaxSize()
                )

                selectedInfo?.let{
                    InfoDialog(info = it, onClose = {selectedInfo = null})
                }
            }
        }
    }
}

@Composable
fun GridLayout(items: List<String>, onItemInfoClick: (String) -> Unit, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(items.size){ index ->
            Card(
               modifier = Modifier
                   .padding(8.dp)
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(10.dp)),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(items[index], fontWeight = FontWeight.Bold)

                        TextButton(onClick = {onItemInfoClick(items[index])}) {
                            Text("info", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoDialog(info:String, onClose: () -> Unit){
    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "More about $info", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "This is more information about $info.", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = onClose) {
                    Text("Close")
                }
            }
        }
    }
}