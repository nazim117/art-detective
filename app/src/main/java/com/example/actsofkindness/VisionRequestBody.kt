package com.example.actsofkindness

import com.google.gson.annotations.SerializedName

data class VisionRequestBody(
    val requests: List<VisionRequest>
)

data class VisionRequest(
    val image: Image,
    val features: List<Feature>
)

data class Image(
    val content: String // Base64 encoded image
)

data class Feature(
    val type: String,
    val maxResults: Int
)

data class VisionResponse(
    val responses: List<AnnotateImageResponse>
)

data class AnnotateImageResponse(
    @SerializedName("webDetection") val webDetection: WebDetection?,
    @SerializedName("labelAnnotations") val labelAnnotations: List<EntityAnnotation>? // Added labelAnnotations here
)

data class WebDetection(
    @SerializedName("webEntities") val webEntities: List<WebEntity>?,
    @SerializedName("bestGuessLabels") val bestGuessLabels: List<BestGuessLabel>?
)

data class WebEntity(
    val description: String?,
    val score: Float?
)

data class BestGuessLabel(
    val label: String?
)

data class EntityAnnotation( // This class represents individual label detections
    val description: String?,
    val score: Float?
)
