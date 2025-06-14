package com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaEnCines

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.robertosanchez.watchinit.data.model.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EnCineViewModel : ViewModel() {
    private val _lista: MutableLiveData<List<MediaItem>> = MutableLiveData()
    val lista: LiveData<List<MediaItem>> = _lista

    private val _progressBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBar: LiveData<Boolean> = _progressBar

    init {
        _progressBar.value = true
        viewModelScope.launch {
            try {
                val allResults = mutableListOf<MediaItem>()

                val hoy = LocalDate.now()
                val haceDosSemanas = hoy.minusWeeks(2)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                for (page in 1..2) {
                    val movies = RemoteConnection.service.peliculasEnCine(
                        apiKey = "49336a7ff05331f9880d3bc4f792f260",
                        page = page
                    )

                    val filteredResults = movies.results.filter { it.release_date != null }.filter { movie ->
                        try {
                            val releaseDate = LocalDate.parse(movie.release_date, formatter)
                            releaseDate in haceDosSemanas..hoy
                        } catch (e: Exception) {
                            false
                        }
                    }

                    val mapped = filteredResults.map {
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

                    allResults.addAll(mapped)
                }

                _lista.value = allResults
            } catch (e: Exception) {
                _lista.value = _lista.value ?: emptyList()
            } finally {
                _progressBar.value = false
            }
        }
    }
}
