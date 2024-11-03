package com.example.actsofkindness

import com.google.firebase.firestore.FirebaseFirestore

class ArtworkRepository(public val firestore: FirebaseFirestore) {

    fun saveArtwork(artwork: ArtObjectAPI, callback: ArtworkFetchCallback) {
        val artworkData = hashMapOf(
            "principalOrFirstMaker" to artwork.principalOrFirstMaker,
            "title" to artwork.title,
            "webImage" to hashMapOf("url" to artwork.webImage?.url)
        )

        firestore.collection("saved_artworks")
            .add(artworkData)
            .addOnSuccessListener {
                fetchSavedArtworks(callback)
            }
            .addOnFailureListener { e ->
                callback.onFailure(e)
            }
    }

    fun removeArtwork(artwork: ArtObjectAPI, callback: ArtworkFetchCallback) {
        firestore.collection("saved_artworks")
            .whereEqualTo("title", artwork.title)
            .get()
            .addOnSuccessListener { snapshot ->
                val deleteTasks = snapshot.documents.map { document ->
                    document.reference.delete()
                }

                callback.onSuccess(emptyList())
            }
            .addOnFailureListener { e ->
                callback.onFailure(e)
            }
    }

    fun fetchSavedArtworks(callback: ArtworkFetchCallback) {
        println("ArtworkRepository.fetchSavedArtworks called") // Debug log
        firestore.collection("saved_artworks")
            .get()
            .addOnSuccessListener { snapshot ->
                val artworkDTOs = snapshot.documents.mapNotNull { document ->
                    document.toObject(ArtObjectAPI::class.java)
                }
                println("Fetched ${artworkDTOs.size} artworks") // Log number of fetched items
                val artworks = artworkDTOs.map { dto ->
                    ArtObjectAPI(
                        title = dto.title,
                        principalOrFirstMaker = dto.principalOrFirstMaker,
                        webImage = dto.webImage?.let { WebImage(url = it.url) }
                    )
                }
                callback.onSuccess(artworks)
            }
            .addOnFailureListener { e ->
                println("Error fetching artworks: ${e.message}") // Log any errors
                callback.onFailure(e)
            }
    }
}