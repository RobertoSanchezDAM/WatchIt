package com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FechaViewModelFactory(private val fecha: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FechaViewModel::class.java)) {
            return FechaViewModel(fecha) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
