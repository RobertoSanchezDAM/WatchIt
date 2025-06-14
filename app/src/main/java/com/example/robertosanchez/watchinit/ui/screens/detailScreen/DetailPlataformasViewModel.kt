package com.example.robertosanchez.watchinit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import com.example.robertosanchez.watchinit.repositories.models.WatchProviders
import kotlinx.coroutines.launch

class DetailPlataformasViewModel : ViewModel() {
    private val _plataformas = MutableLiveData<WatchProviders?>()
    val plataformas: LiveData<WatchProviders?> = _plataformas

    fun fetchPlataformas(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getPlataformas(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _plataformas.value = response
            } catch (e: Exception) {
                _plataformas.value = null
            }
        }
    }
} 