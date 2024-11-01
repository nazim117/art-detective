package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.actsofkindness.Category

@Composable
fun CategoryGrid(categories: List<Category>, navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(16.dp)) {
        items(categories.size) { index ->
            val category = categories[index]
            TextButton(onClick = { navController.navigate("results/${category.name}") }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    category.imageUrl?.let { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = category.name,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(4.dp)
                        )
                    }
                    Text(category.name, textAlign = TextAlign.Center)
                }
            }
        }
    }
}