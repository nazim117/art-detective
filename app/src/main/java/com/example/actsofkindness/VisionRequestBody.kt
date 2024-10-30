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
    val type: String = "WEB_DETECTION",
    val maxResults: Int = 10
)

// Response data classes
data class VisionResponse(
    val responses: List<AnnotateImageResponse>
)

data class AnnotateImageResponse(
    @SerializedName("webDetection") val webDetection: WebDetection?
)

data class WebDetection(
    @SerializedName("webEntities") val webEntities: List<WebEntity>?,
    @SerializedName("bestGuessLabels") val bestGuessLabels: List<BestGuessLabel>?
)

data class WebEntity(val description: String?, val score: Float?)
data class BestGuessLabel(val label: String?)
