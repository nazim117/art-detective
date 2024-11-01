package com.example.actsofkindness

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout

class ArtworkRepository(private val firestore: FirebaseFirestore) {

    suspend fun saveArtwork(artwork: ArtObject): Boolean {
        return try {
            val artworkData = hashMapOf(
                "artist" to artwork.principalOrFirstMaker,
                "imageUrl" to artwork.webImage?.url,
                "title" to artwork.title
            )
            withTimeout(10000) { // Set a 5-second timeout
                firestore
                    .collection("saved_artworks")
                    .add(artworkData)
                    .await()
            }

            true
        } catch (e: Exception) {
            false // Indicate failure
        }
    }

    suspend fun removeArtwork(artwork: ArtObject) {
        try {
            val snapshot = firestore.collection("saved_artworks")
                .whereEqualTo("title", artwork.title)
                .get()
                .await()
            for (document in snapshot.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            // Handle exceptions if needed
        }
    }

    suspend fun fetchSavedArtworks(): List<ArtObject> {
        return try {
            val snapshot = firestore.collection("saved_artworks").get().await()
            snapshot.documents.mapNotNull { it.toObject<ArtObject>() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
