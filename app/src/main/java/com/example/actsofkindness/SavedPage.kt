package com.example.actsofkindness

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun SavedPage(navController: NavController) {
    var selectedInfo by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = {Text("Your Saved Photos")}
        )

        GridLayout(items = listOf("Photo 1", "Photo 2", "Photo 3"),
            onItemInfoClick = {info ->
                selectedInfo = info
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Saved Art", style = MaterialTheme.typography.bodyLarge)
        GridLayout(
            items = listOf("Art 1", "Art 2", "Art 3", "Art 1", "Art 2", "Art 3"),
            onItemInfoClick = {info ->
                selectedInfo = info
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}