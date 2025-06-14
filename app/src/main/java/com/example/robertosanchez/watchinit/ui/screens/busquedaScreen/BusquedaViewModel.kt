package com.example.robertosanchez.watchinit.ui.screens.busquedaScreen

import com.example.robertosanchez.watchinit.data.model.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import kotlinx.coroutines.launch

class BusquedaViewModel(private var pelicula: String) : ViewModel() {

    private val _lista: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val lista: LiveData<List<MediaItem>> = _lista

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    init {
        if (pelicula.isNotBlank()) {
            buscarPeliculas(pelicula)
        }
    }

    fun buscarPeliculas(pelicula: String) {
        if (pelicula.isBlank()) return
        
        _progressBar.value = true
        viewModelScope.launch {
            val allMovies = mutableMapOf<Int, MediaItem>()
            try {
                for (page in 1..3) {
                    val responseES = RemoteConnection.service.buscarPeliculas(
                        query = pelicula,
                        apiKey = "49336a7ff05331f9880d3bc4f792f260",
                        language = "es-ES",
                        page = page
                    )
                    responseES.results.forEach {
                        allMovies[it.id] = MediaItem(
                            it.id,
                            "https://image.tmdb.org/t/p/w185${it.poster_path}",
                            it.release_date,
                            it.title,
                            it.overview,
                            "https://image.tmdb.org/t/p/w185${it.backdrop_path}",
                            it.genre_ids
                        )
                    }
                }

                for (page in 1..3) {
                    val responseEN = RemoteConnection.service.buscarPeliculas(
                        query = pelicula,
                        apiKey = "49336a7ff05331f9880d3bc4f792f260",
                        language = "en-US",
                        page = page
                    )
                    responseEN.results.forEach {
                        if (!allMovies.containsKey(it.id)) {
                            allMovies[it.id] = MediaItem(
                                it.id,
                                "https://image.tmdb.org/t/p/w185${it.poster_path}",
                                it.release_date,
                                it.title,
                                it.overview,
                                "https://image.tmdb.org/t/p/w185${it.backdrop_path}",
                                it.genre_ids
                            )
                        }
                    }
                }

                _lista.value = allMovies.values.toList()
            } catch (e: Exception) {
                _lista.value = _lista.value ?: emptyList()
            } finally {
                _progressBar.value = false
            }
        }
    }
}
