package com.example.actsofkindness

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun NavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = {Icon(Icons.Default.Build, contentDescription = "Camera") },
            label = { Text("Camera")},
            selected = false,
            onClick={navController.navigate("camera")}
        )
        NavigationBarItem(
            icon = {Icon(Icons.Default.Search, contentDescription = "Explore") },
            label = { Text("Explore")},
            selected = false,
            onClick={navController.navigate("explore")}
        )
        NavigationBarItem(
            icon = {Icon(Icons.Default.Favorite, contentDescription = "Saved") },
            label = { Text("Saved")},
            selected = false,
            onClick={navController.navigate("saved")}
        )
    }
}