package com.example.robertosanchez.watchinit.ui.screens.busquedaScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BusquedaViewModelFactory(private val pelicula: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusquedaViewModel::class.java)) {
            return BusquedaViewModel(pelicula) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
