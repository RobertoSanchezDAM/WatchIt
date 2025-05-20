package com.example.robertosanchez.watchit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.repositories.RemoteConnection
import com.example.robertosanchez.watchit.repositories.models.MovieImagesResponse
import kotlinx.coroutines.launch

class DetailImagesViewModel : ViewModel() {
    private val _images = MutableLiveData<MovieImagesResponse?>()
    val images: LiveData<MovieImagesResponse?> = _images

    fun fetchImages(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getImages(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _images.value = response
            } catch (e: Exception) {
                _images.value = null
            }
        }
    }
} 