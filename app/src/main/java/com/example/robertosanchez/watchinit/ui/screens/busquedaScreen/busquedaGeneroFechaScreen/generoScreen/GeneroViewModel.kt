package com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen

import com.example.robertosanchez.watchinit.data.model.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import kotlinx.coroutines.launch

class GeneroViewModel(private var genero: Int) : ViewModel() {

    private val _lista: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val lista: LiveData<List<MediaItem>> = _lista

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    val generoSeleccionado: Int = genero

    init {
        if (genero != 0) {
            buscarGenero(genero)
        }
    }

    fun buscarGenero(genero: Int) {
        if (genero == 0) return

        _progressBar.value = true
        viewModelScope.launch {
            val allMovies = mutableListOf<MediaItem>()
            try {
                for (page in 1..20) {
                    val response = RemoteConnection.service.peliculaGenero(
                        apiKey = "49336a7ff05331f9880d3bc4f792f260",
                        page = page,
                        genre = genero
                    )
                    val mappedMovies = response.results.map {
                        MediaItem(
                            it.id,
                            "https://image.tmdb.org/t/p/w185${it.poster_path}",
                            it.release_date,
                            it.title,
                            it.overview,
                            "https://image.tmdb.org/t/p/w185${it.backdrop_path}",
                            it.genre_ids
                        )
                    }
                    allMovies.addAll(mappedMovies)
                }

                _lista.value = allMovies
            } catch (e: Exception) {
                _lista.value = _lista.value ?: emptyList()
            } finally {
                _progressBar.value = false
            }
        }
    }
}
