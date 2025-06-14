package com.example.robertosanchez.watchinit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import com.example.robertosanchez.watchinit.repositories.models.MovieImagesResponse
import kotlinx.coroutines.launch

class DetailImagenesViewModel : ViewModel() {
    private val _imagenes = MutableLiveData<MovieImagesResponse?>()
    val imagenes: LiveData<MovieImagesResponse?> = _imagenes

    fun fetchImagenes(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getImagenes(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _imagenes.value = response
            } catch (e: Exception) {
                _imagenes.value = null
            }
        }
    }
} 