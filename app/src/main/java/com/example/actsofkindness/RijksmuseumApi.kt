package com.example.actsofkindness

import retrofit2.http.GET
import retrofit2.http.Query

interface RijksmuseumApi {
    @GET("api/en/collection")
    suspend fun searchArtworks(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("imgonly") imgOnly: Boolean = true
    ): RijksmuseumResponse
}