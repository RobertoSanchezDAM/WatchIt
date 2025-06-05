package com.example.robertosanchez.watchit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.repositories.RemoteConnection
import com.example.robertosanchez.watchit.repositories.models.MovieVideosResponse
import kotlinx.coroutines.launch

class DetailVideosViewModel : ViewModel() {
    private val _videos = MutableLiveData<MovieVideosResponse?>()
    val videos: LiveData<MovieVideosResponse?> = _videos

    fun fetchVideos(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getVideos(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _videos.value = response
            } catch (e: Exception) {
                _videos.value = null
            }
        }
    }
} 