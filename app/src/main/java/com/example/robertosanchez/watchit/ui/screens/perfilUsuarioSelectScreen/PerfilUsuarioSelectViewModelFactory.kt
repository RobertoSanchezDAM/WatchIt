package com.example.robertosanchez.watchit.ui.screens.perfilUsuarioSelectScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robertosanchez.watchit.db.FirestoreManager

class PerfilUsuarioSelectViewModelFactory(
    private val firestore: FirestoreManager,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilUsuarioSelectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilUsuarioSelectViewModel(firestore, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 