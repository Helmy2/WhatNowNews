package com.example.whatnownews.domain.models.articles

data class FavoriteArticlesModel(
    val id: String = "",
    val url: String = "",
    val title: String = "",
    val urlToImage: String = "",
    var isFavorite: Boolean = true
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "url" to url,
            "urlToImage" to urlToImage,
            "isFavorite" to isFavorite
        )
    }
}