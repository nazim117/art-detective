package com.example.actsofkindness

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = {Icon(Icons.Default.Call, contentDescription = "Camera") },
            label = { Text("Camera")},
            selected = false,
            onClick={navController.navigate("camera")}
        )
        NavigationBarItem(
            icon = {Icon(Icons.Default.Home, contentDescription = "Explore") },
            label = { Text("Explore")},
            selected = false,
            onClick={navController.navigate("explore")}
        )
        NavigationBarItem(
            icon = {Icon(Icons.Default.AccountBox, contentDescription = "Saved") },
            label = { Text("Saved")},
            selected = false,
            onClick={navController.navigate("saved")}
        )
    }
}