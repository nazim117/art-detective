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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePage(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        TopAppBar(
            title = { Text("Explore")},
            actions = {
                TextButton(onClick = {}) {
                    Text("View All")
                }
            }
        )

        Text("Categories", style = MaterialTheme.typography.bodySmall)

        GridLayout(
            items= listOf("Art 1", "Art 2", "Art 3", "Art 4"),
            modifier = Modifier
                .height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Popular Artists", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = {}) {
                Text("View All")
            }
        }
        GridLayout(
            items = listOf("Artist 1", "Artist 2", "Artist 3"),
            modifier = Modifier
                .height(200.dp))

        Spacer(modifier = Modifier.weight(1f))
        NavigationBar(navController = navController)
    }
}

@Composable
fun GridLayout(items: List<String>, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items.size){ index ->
            Card(
               modifier = Modifier
                   .padding(8.dp)
                   .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                ){
                    Text(items[index])
                }
            }
        }
    }
}