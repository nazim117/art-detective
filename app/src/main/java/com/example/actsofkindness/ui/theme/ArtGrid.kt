package com.example.actsofkindness.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.actsofkindness.ArtObject
import com.example.actsofkindness.ArtViewModel

@Composable
fun ArtGrid(
    artObjects: List<ArtObject>,
    viewModel: ArtViewModel? = null,
    onInfoClick: (ArtObject) -> Unit,
    onSaveClick: (ArtObject) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(artObjects) { artwork ->
            ArtworkCard(
                artwork = artwork,
                viewModel = viewModel,
                onInfoClick = onInfoClick,
                onSaveClick = onSaveClick
            )
        }
    }
}