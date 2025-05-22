package com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GeneroViewModelFactory(private val genero: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneroViewModel::class.java)) {
            return GeneroViewModel(genero) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
