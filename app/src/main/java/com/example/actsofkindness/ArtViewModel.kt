package com.example.actsofkindness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Category(val name: String, val imageUrl: String?)
data class Artist(val name: String, val imageUrl: String?)

class ArtViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    private val _artObjects = MutableStateFlow<List<ArtObject>>(emptyList())
    val artObjects: StateFlow<List<ArtObject>> get() = _artObjects

    private val apiKey = "L1aZpugQ"

    // Fetch random categories by querying popular art styles
    fun fetchRandomCategories() {
        viewModelScope.launch {
            try {
                val artStyles = listOf("Impressionism", "Renaissance", "Baroque", "Cubism", "Surrealism", "Realism")
                val categories = mutableListOf<Category>()

                // Retrieve image for each category style
                for (style in artStyles) {
                    val response = RetrofitInstance.api.searchArtworks(
                        apiKey = apiKey,
                        query = style,
                        imgOnly = true
                    )

                    // Take the first artwork's image as representative
                    response.artObjects.firstOrNull()?.let { artObject ->
                        categories.add(
                            Category(
                                name = style,
                                imageUrl = artObject.webImage?.url
                            )
                        )
                    }
                }
                _categories.value = categories
            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }

    // Fetch popular artists by querying for well-known artist names
    fun fetchPopularArtists() {
        viewModelScope.launch {
            try {
                val famousArtists = listOf("Rembrandt", "Vermeer", "Picasso", "Monet", "Van Gogh", "Dali")
                val artists = mutableListOf<Artist>()

                // Retrieve image for each artist
                for (artist in famousArtists) {
                    val response = RetrofitInstance.api.searchArtworks(
                        apiKey = apiKey,
                        query = artist,
                        imgOnly = true
                    )

                    // Take the first artwork's image as representative
                    response.artObjects.firstOrNull()?.let { artObject ->
                        artists.add(
                            Artist(
                                name = artist,
                                imageUrl = artObject.webImage?.url
                            )
                        )
                    }
                }
                _artists.value = artists
            } catch (e: Exception) {
                _artists.value = emptyList()
            }
        }
    }

    // Fetch artworks based on a specific category or artist
    fun fetchArtworks(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchArtworks(
                    apiKey = apiKey,
                    query = query,
                    imgOnly = true
                )
                _artObjects.value = response.artObjects
            } catch (e: Exception) {
                _artObjects.value = emptyList()
            }
        }
    }
}
