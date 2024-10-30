package com.example.actsofkindness

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationBar(navController = navController) },
        modifier = Modifier.fillMaxSize()
    ){ innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ){
            NavHost(
                navController = navController,
                startDestination = "explore",
                modifier = Modifier.fillMaxSize()
            ){
                composable("explore") { ExplorePage(navController)}
                composable("saved") { SavedPage(navController)}
                composable("camera") { CameraPage(navController)}
            }
        }
    }
}