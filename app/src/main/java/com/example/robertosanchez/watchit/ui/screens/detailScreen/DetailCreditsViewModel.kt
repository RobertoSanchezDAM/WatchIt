package com.example.robertosanchez.watchit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.repositories.RemoteConnection
import com.example.robertosanchez.watchit.repositories.models.CreditsResponse
import kotlinx.coroutines.launch

class DetailCreditsViewModel : ViewModel() {
    private val _credits = MutableLiveData<CreditsResponse?>()
    val credits: LiveData<CreditsResponse?> = _credits

    fun fetchCredits(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getMovieCredits(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _credits.value = response
            } catch (e: Exception) {
                _credits.value = null
            }
        }
    }
} 