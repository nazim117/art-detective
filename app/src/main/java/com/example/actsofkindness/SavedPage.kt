package com.example.actsofkindness

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(navController: NavController) {
    Column {
        TopAppBar(
            title = {Text("Your Saved Photos")}
        )

        GridLayout(items = listOf("Photo 1", "Photo 2", "Photo 3"))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Saved Art", style = MaterialTheme.typography.bodySmall)
        GridLayout(items = listOf("Art 1", "Art 2", "Art 3", "Art 1", "Art 2", "Art 3"))

        Spacer(modifier = Modifier.weight(1f))
        NavigationBar(navController = navController)
    }
}