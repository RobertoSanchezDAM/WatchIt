package com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager

class PeliculasVistasViewModelFactory(private val firestore: FirestoreManager, private val authManager: AuthManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculasVistasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculasVistasViewModel(firestore, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}