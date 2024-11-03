package com.example.actsofkindness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ArtViewModel(
    private val artworkRepository: ArtworkRepository = ArtworkRepository(FirebaseFirestore.getInstance())
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val _showSaveSnackbar = MutableStateFlow(false)
    val showSaveSnackbar: StateFlow<Boolean> get() = _showSaveSnackbar

    private val _savedCaptures = MutableStateFlow<List<CaptureObject>>(emptyList())
    val savedCaptures: StateFlow<List<CaptureObject>> get() = _savedCaptures

    private val _savedArtworks = MutableStateFlow<List<ArtObjectAPI>>(emptyList())
    val savedArtworks: StateFlow<List<ArtObjectAPI>> get() = _savedArtworks

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    private val _artObjectsAPI = MutableStateFlow<List<ArtObjectAPI>>(emptyList())
    val artObjectsAPI: StateFlow<List<ArtObjectAPI>> get() = _artObjectsAPI

    private val _isLoadingCategories = MutableStateFlow(false)
    private val _isLoadingArtists = MutableStateFlow(false)
    private val apiKey = "2zWzO88Q"

    fun fetchRandomCategories() {
        viewModelScope.launch {
            _isLoadingCategories.value = true
            try {
                val artStyles = listOf(
                    "Impressionism",
                    "Renaissance",
                    "Baroque",
                    "Cubism",
                    "Surrealism",
                    "Realism"
                )
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
                ApiCache.categories = categories
            } catch (e: Exception) {
                println("Error fetching categories from API: ${e.message}")
                ApiCache.categories?.let {
                    _categories.value = it
                } ?: run {
                    _categories.value = emptyList()
                }
            } finally {
                _isLoadingCategories.value = false
            }
        }
    }

    fun fetchPopularArtists() {
        viewModelScope.launch {
            _isLoadingArtists.value = true
            try {
                val famousArtists =
                    listOf("Rembrandt", "Vermeer", "Picasso", "Monet", "Van Gogh", "Dali")
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
                ApiCache.artists = artists
            } catch (e: Exception) {
                println("Error fetching artists from API: ${e.message}")
                ApiCache.artists?.let {
                    _artists.value = it
                } ?: run {
                    _artists.value = emptyList()
                }
            } finally {
                _isLoadingArtists.value = false
            }
        }
    }

    fun fetchArtworks(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchArtworks(
                    apiKey = apiKey,
                    query = query,
                    imgOnly = true
                )
                _artObjectsAPI.value = response.artObjects
            } catch (e: Exception) {
                println("Error fetching artworks: ${e.message}")
                _artObjectsAPI.value = emptyList()
            }
        }
    }

    fun saveArtwork(artwork: ArtObjectAPI) {
        _isLoading.value = true
        artworkRepository.saveArtwork(artwork, object : ArtworkFetchCallback {
            override fun onSuccess(artworks: List<ArtObjectAPI>) {
                _showSaveSnackbar.value = true
                fetchSavedArtworks()
            }

            override fun onFailure(e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        })
    }

    fun toggleSaveArtwork(artwork: ArtObjectAPI) {
        viewModelScope.launch {
            val existingArtwork = _savedArtworks.value.find { it.title == artwork.title }
            if (existingArtwork != null) {
                removeArtwork(existingArtwork)
            } else {
                saveArtwork(artwork)
            }
        }
    }

    private fun removeArtwork(artwork: ArtObjectAPI) {
        _isLoading.value = true
        artworkRepository.removeArtwork(artwork, object : ArtworkFetchCallback {
            override fun onSuccess(artworks: List<ArtObjectAPI>) {
                fetchSavedArtworks()
            }

            override fun onFailure(e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        })
    }

    fun fetchSavedArtworks() {
        _isLoading.value = true
        println("fetchSavedArtworks called") // Debug log to confirm it's called
        artworkRepository.fetchSavedArtworks(object : ArtworkFetchCallback {
            override fun onSuccess(artworks: List<ArtObjectAPI>) {
                println("fetchSavedArtworks onSuccess: ${artworks.size} items") // Log success
                _savedArtworks.value = artworks
                _isLoading.value = false
            }

            override fun onFailure(e: Exception) {
                println("fetchSavedArtworks onFailure: ${e.message}") // Log failure
                _isLoading.value = false
                e.printStackTrace()
            }
        })
    }

    fun fetchSavedCaptures() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                println("Debug: Fetching captures from Firestore...")

                val snapshot =
                    artworkRepository.firestore.collection("saved_captures").get().await()
                val captures = snapshot.documents.mapNotNull { document ->
                    val description = document.getString("description").orEmpty()
                    val imageBase64 = document.getString("imageBase64").orEmpty()

                    // Debug each capture's content to verify data integrity
                    println("Debug: Fetched capture - Description: $description, ImageBase64 Length: ${imageBase64.length}")

                    if (description.isNotBlank() && imageBase64.isNotBlank()) {
                        CaptureObject(description = description, imageBase64 = imageBase64)
                    } else {
                        null
                    }
                }

                _savedCaptures.value = captures
                _isLoading.value = false

                println("Debug: Fetched ${captures.size} captures from Firestore.")
                captures.forEach { capture -> println("Capture - Description: ${capture.description}") }

            } catch (e: Exception) {
                e.printStackTrace()
                _savedCaptures.value = emptyList()
                _isLoading.value = false
                println("Error: Failed to fetch captures - ${e.message}")
            }
        }
    }
}
