package com.example.whatnownews.data.remote.favorites

import android.util.Log
import com.example.whatnownews.domain.model.FavoriteArticlesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteArticlesDS(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val userId = firebaseAuth.currentUser?.uid.toString()
    suspend fun updateFavorite(articleId: String, isFav: Boolean) {
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("favorite_articles")
            .document(articleId)

        try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                docRef.update("isFavorite", isFav).await()
                Log.wtf("🔥 Updated", "$articleId -> $isFav")
            } else {
                docRef.set(mapOf("id" to articleId, "isFavorite" to isFav)).await()
                Log.wtf("✨ Created new", "$articleId -> $isFav")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Update failed", e)
        }
    }


    suspend fun addFavorite(article: FavoriteArticlesModel) {
        try {
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("favorite_articles")
                .document(article.id)

            docRef.set(article.toMap()).await()

            Log.wtf("✨ Added favorite", "${article.id} -> ${article.isFavorite}")
        } catch (e: Exception) {
            Log.e("Firestore", "Add favorite failed", e)
        }
    }


    fun listenToFavoriteArticles(onResult: (List<FavoriteArticlesModel>) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .collection("favorite_articles")
            .whereEqualTo("isFavorite", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val articles = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FavoriteArticlesModel::class.java)
                        ?.copy(id = doc.id)
                } ?: emptyList()

                Log.e("🔥 ListenResult", "Fetched IDs: ${articles.joinToString { it.id }}")
                onResult(articles)
            }
    }


    suspend fun getArticles(): List<FavoriteArticlesModel> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("favorite_articles")
            .whereEqualTo("isFavorite", true)
            .get()
            .await()

        val articles = snapshot.documents.mapNotNull { doc ->
            doc.toObject(FavoriteArticlesModel::class.java)?.copy(id = doc.id)
        }

        Log.e("🔥 GetArticles", "Fetched IDs: ${articles.joinToString { it.id }}")
        return articles
    }
}
