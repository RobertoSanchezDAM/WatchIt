package com.example.robertosanchez.watchinit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.data.model.MediaItem
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import kotlinx.coroutines.launch

class DetailGeneroViewModel : ViewModel() {
    private val _lista: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val lista: LiveData<List<MediaItem>> = _lista

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    fun buscarPeliculaPorId(id: Int) {
        _progressBar.value = true
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.peliculaDetalle(
                    movieId = id,
                    apiKey = "49336a7ff05331f9880d3bc4f792f260"
                )

                val pelicula = MediaItem(
                    response.id,
                    "https://image.tmdb.org/t/p/w185${response.poster_path}",
                    response.release_date,
                    response.title,
                    response.overview,
                    "https://image.tmdb.org/t/p/w185${response.backdrop_path}",
                    response.genres.map { it.id }
                )
                _lista.value = listOf(pelicula)
            } catch (e: Exception) {
                _lista.value = emptyList()
            } finally {
                _progressBar.value = false
            }
        }
    }
} 