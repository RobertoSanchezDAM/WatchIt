package com.example.robertosanchez.watchit.ui.screens.perfilScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robertosanchez.watchit.db.FirestoreManager

class PeliculasFavoritasViewModelFactory(private val firestore: FirestoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculasFavoritasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculasFavoritasViewModel(firestore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
