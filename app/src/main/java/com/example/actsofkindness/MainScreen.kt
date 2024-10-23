package com.example.actsofkindness

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "explore"){
        composable("explore") { ExplorePage(navController)}
        composable("saved") { SavedPage(navController)}
    }
}