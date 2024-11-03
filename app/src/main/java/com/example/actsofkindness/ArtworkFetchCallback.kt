package com.example.actsofkindness

interface ArtworkFetchCallback {
    fun onSuccess(artworks: List<ArtObjectAPI>)
    fun onFailure(e: Exception)
}