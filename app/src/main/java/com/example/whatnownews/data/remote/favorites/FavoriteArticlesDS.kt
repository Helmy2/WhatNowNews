package com.example.whatnownews.data.remote.favorites

import android.util.Log
import com.example.whatnownews.BuildConfig
import com.example.whatnownews.domain.models.articles.FavoriteArticlesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteArticlesDS(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val _userId = firebaseAuth.currentUser?.uid.toString()
    suspend fun updateFavorite(articleId: String, isFav: Boolean) {
        val safeId = articleId.replace("/", "_")

        val docRef = firestore.collection("users")
            .document(_userId)
            .collection("favorite_articles")
            .document(safeId)

        try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                docRef.update("isFavorite", isFav).await()
            } else {
                docRef.set(mapOf("id" to articleId, "isFavorite" to isFav)).await()
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("Firestore", "Error updating favorite", e)
            }
        }
    }

    suspend fun addFavorite(article: FavoriteArticlesModel) {
        val safeId = article.id.replace("/", "_")

        val docRef = firestore.collection("users")
            .document(_userId)
            .collection("favorite_articles")
            .document(safeId)

        try {
            val snapshot = docRef.get().await()

            if (snapshot.exists()) {
                docRef.update("isFavorite", article.isFavorite).await()
            } else {
                docRef.set(article.toMap()).await()
            }

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("Firestore", "Error updating favorite", e)
            }
        }
    }




    fun listenToFavoriteArticles(onResult: (List<FavoriteArticlesModel>) -> Unit) {
        firestore.collection("users")
            .document(_userId)
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

                onResult(articles)
            }
    }


    suspend fun getArticles(): List<FavoriteArticlesModel> {
        val snapshot = firestore.collection("users")
            .document(_userId)
            .collection("favorite_articles")
            .whereEqualTo("isFavorite", true)
            .get()
            .await()

        val articles = snapshot.documents.mapNotNull { doc ->
            doc.toObject(FavoriteArticlesModel::class.java)?.copy(id = doc.id)
        }

        return articles
    }
}
