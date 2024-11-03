package com.example.actsofkindness

data class RijksmuseumResponse(
    val artObjects: List<ArtObjectAPI> = emptyList()
)

data class ArtObjectAPI(
    val id: String = "",
    val title: String = "",
    val principalOrFirstMaker: String = "",
    val webImage: WebImage? = null
)

data class WebImage(val url: String = "")

data class Category(val name: String, val imageUrl: String?)
data class Artist(val name: String, val imageUrl: String?)