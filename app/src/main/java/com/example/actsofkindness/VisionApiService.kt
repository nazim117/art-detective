package com.example.actsofkindness

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface VisionApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/images:annotate")
    fun analyzeImage(
        @Body body: VisionRequestBody,
        @Query("key") apiKey: String
    ): Call<VisionResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://vision.googleapis.com/"

    val instance: VisionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VisionApiService::class.java)
    }
}
