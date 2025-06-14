package com.example.robertosanchez.watchinit.ui.screens.reviewsUsuarioScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager

class ReviewsUsuarioViewModelFactory(
    private val firestore: FirestoreManager,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewsUsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewsUsuarioViewModel(firestore, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 