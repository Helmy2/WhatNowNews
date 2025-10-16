package com.example.whatnownews.domain.models.category

sealed class Category(val categoryName: String) {
    object General : Category("general")
    object Entertainment : Category("entertainment")
    object Sports : Category("sports")
    object Business : Category("business")
    object Health : Category("health")
    object Science : Category("science")
}