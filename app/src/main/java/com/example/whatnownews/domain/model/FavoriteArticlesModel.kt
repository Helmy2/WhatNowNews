package com.example.whatnownews.domain.model

data class FavoriteArticlesModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    var isFavorite: Boolean = true
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "imageUrl" to imageUrl,
            "isFavorite" to isFavorite
        )
    }
}
