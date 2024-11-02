package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.actsofkindness.CameraPage

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationBar(navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = "camera",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("camera") { CameraPage(navController) }
                composable("explore") { ExplorePage(navController) }
                composable("saved") { SavedPage(navController) }

                composable(
                    route = "results/{category}",
                    arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStackEntry ->
                    val category = backStackEntry.arguments?.getString("category") ?: ""
                    ResultsPage(category = category, navController = navController)
                }
            }
        }
    }
}
