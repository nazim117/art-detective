package com.example.actsofkindness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Category(val name: String, val imageUrl: String?)
data class Artist(val name: String, val imageUrl: String?)

class ArtViewModel(
    private val artworkRepository: ArtworkRepository = ArtworkRepository(FirebaseFirestore.getInstance())
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val _showSaveSnackbar = MutableStateFlow(false)
    val showSaveSnackbar: StateFlow<Boolean> get() = _showSaveSnackbar

    private val _savedArtworks = MutableStateFlow<List<ArtObject>>(emptyList())
    val savedArtworks: StateFlow<List<ArtObject>> get() = _savedArtworks

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    private val _artObjects = MutableStateFlow<List<ArtObject>>(emptyList())
    val artObjects: StateFlow<List<ArtObject>> get() = _artObjects

    private val apiKey = "2zWzO88Q"

    fun fetchRandomCategories() {
        viewModelScope.launch {
            try {
                /*
                val artStyles = listOf("Impressionism", "Renaissance", "Baroque", "Cubism", "Surrealism", "Realism")
                val categories = mutableListOf<Category>()

                for (style in artStyles) {
                    val response = RetrofitInstance.api.searchArtworks(
                        apiKey = apiKey,
                        query = style,
                        imgOnly = true
                    )
                    response.artObjects.firstOrNull()?.let { artObject ->
                        categories.add(Category(name = style, imageUrl = artObject.webImage?.url))
                    }
                }
                _categories.value = categories
                */

                // Hardcoded data for categories
                _categories.value = listOf(
                    Category("Impressionism", "https://example.com/impressionism.jpg"),
                    Category("Renaissance", "https://example.com/renaissance.jpg"),
                    Category("Baroque", "https://example.com/baroque.jpg"),
                    Category("Cubism", "https://example.com/cubism.jpg"),
                    Category("Surrealism", "https://example.com/surrealism.jpg"),
                    Category("Realism", "https://example.com/realism.jpg")
                )

            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }

    fun fetchPopularArtists() {
        viewModelScope.launch {
            try {
                /*
                val famousArtists = listOf("Rembrandt", "Vermeer", "Picasso", "Monet", "Van Gogh", "Dali")
                val artists = mutableListOf<Artist>()

                for (artist in famousArtists) {
                    val response = RetrofitInstance.api.searchArtworks(
                        apiKey = apiKey,
                        query = artist,
                        imgOnly = true
                    )
                    response.artObjects.firstOrNull()?.let { artObject ->
                        artists.add(Artist(name = artist, imageUrl = artObject.webImage?.url))
                    }
                }
                _artists.value = artists
                */

                // Hardcoded data for artists
                _artists.value = listOf(
                    Artist("Rembrandt", "https://example.com/rembrandt.jpg"),
                    Artist("Vermeer", "https://example.com/vermeer.jpg"),
                    Artist("Picasso", "https://example.com/picasso.jpg"),
                    Artist("Monet", "https://example.com/monet.jpg"),
                    Artist("Van Gogh", "https://example.com/vangogh.jpg"),
                    Artist("Dali", "https://example.com/dali.jpg")
                )

            } catch (e: Exception) {
                _artists.value = emptyList()
            }
        }
    }

    fun fetchArtworks(query: String) {
        viewModelScope.launch {
            try {
                /*
                val response = RetrofitInstance.api.searchArtworks(
                    apiKey = apiKey,
                    query = query,
                    imgOnly = true
                )
                _artObjects.value = response.artObjects
                */

                // Hardcoded data for artworks
                _artObjects.value = listOf(
                    ArtObject("1", "Starry Night", "maker 1", WebImage("https://example.com/starrynight.jpg")),
                    ArtObject("2", "The Persistence of Memory", "maker 2", WebImage("https://example.com/persistence.jpg")),
                    ArtObject("3","Girl with a Pearl Earring", "maker 3", WebImage("https://example.com/girlwithpearl.jpg")),
                    ArtObject("4","Guernica", "maker 4", WebImage("https://example.com/guernica.jpg")),
                    ArtObject("5","The Night Watch", "maker 5", WebImage("https://example.com/nightwatch.jpg"))
                )

            } catch (e: Exception) {
                _artObjects.value = emptyList()
            }
        }
    }

    fun saveArtwork(artwork: ArtObject) {
        viewModelScope.launch {
            val success = artworkRepository.saveArtwork(artwork)
            if (success) {
                _showSaveSnackbar.value = true
                fetchSavedArtworks()
            }
        }
    }

    fun toggleSaveArtwork(artwork: ArtObject) {
        viewModelScope.launch {
            val existingArtwork = _savedArtworks.value.find { it.title == artwork.title }
            if (existingArtwork != null) {
                artworkRepository.removeArtwork(existingArtwork)
                fetchSavedArtworks()
            } else {
                saveArtwork(artwork)
            }
        }
    }

    fun fetchSavedArtworks() {
        viewModelScope.launch {
            _isLoading.value = true
            _savedArtworks.value = artworkRepository.fetchSavedArtworks()
            _isLoading.value = false
        }
    }
}
