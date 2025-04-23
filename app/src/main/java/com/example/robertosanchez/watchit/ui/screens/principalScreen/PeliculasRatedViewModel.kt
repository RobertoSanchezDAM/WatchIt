package com.example.robertosanchez.watchit.ui.screens.principalScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.proyectoapi.repositories.RemoteConnection
import com.example.robertosanchez.watchit.data.model.Peliculas
import kotlinx.coroutines.launch

class PeliculasRatedViewModel: ViewModel() {
    private val _lista: MutableLiveData<List<Peliculas>> = MutableLiveData()
    val lista: LiveData<List<Peliculas>> = _lista

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    init {
        _progressBar.value = true
        viewModelScope.launch() {
            val movies = RemoteConnection.service.ratedMovies("49336a7ff05331f9880d3bc4f792f260")
            _lista.value = movies.results.map {
                Peliculas(
                    it.id,
                    "https://image.tmdb.org/t/p/w185" + it.poster_path
                )
            }
            _progressBar.value = false
        }
    }
}
