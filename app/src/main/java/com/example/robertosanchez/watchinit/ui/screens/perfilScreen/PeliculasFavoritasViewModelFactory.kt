package com.example.robertosanchez.watchinit.ui.screens.perfilScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager

class PeliculasFavoritasViewModelFactory(private val firestore: FirestoreManager, private val authManager: AuthManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculasFavoritasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculasFavoritasViewModel(firestore, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}