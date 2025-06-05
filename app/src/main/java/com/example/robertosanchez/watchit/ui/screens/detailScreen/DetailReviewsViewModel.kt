package com.example.robertosanchez.watchit.ui.screens.detailScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.repositories.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetailReviewsViewModel : ViewModel() {
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val firestore = FirebaseFirestore.getInstance()
    private val reviewsCollection = firestore.collection("reviews")

    fun loadReviews(movieId: Int) {
        viewModelScope.launch {
            try {
                Log.d("DetailReviewsViewModel", "Cargando reviews para la película: $movieId")
                // Temporalmente, solo filtramos por movieId sin ordenar
                val snapshot = reviewsCollection
                    .whereEqualTo("movieId", movieId)
                    .get()
                    .await()

                val reviewsList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.copy(id = doc.id)
                }.sortedByDescending { it.timestamp } // Ordenamos en memoria

                Log.d("DetailReviewsViewModel", "Reviews cargadas: ${reviewsList.size}")
                _reviews.value = reviewsList
            } catch (e: Exception) {
                Log.e("DetailReviewsViewModel", "Error al cargar reviews: ${e.message}", e)
                _reviews.value = emptyList()
            }
        }
    }

    fun addReview(review: Review, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("DetailReviewsViewModel", "Añadiendo nueva review para la película: ${review.movieId}")
                val docRef = reviewsCollection.add(review).await()
                Log.d("DetailReviewsViewModel", "Review añadida con ID: ${docRef.id}")
                loadReviews(review.movieId)
                onSuccess()
            } catch (e: Exception) {
                Log.e("DetailReviewsViewModel", "Error al añadir review: ${e.message}", e)
                onError(e.message ?: "Error al añadir la review")
            }
        }
    }
} 