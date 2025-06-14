package com.example.robertosanchez.watchinit.ui.screens.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.repositories.RemoteConnection
import com.example.robertosanchez.watchinit.repositories.models.CreditsResponse
import kotlinx.coroutines.launch

class DetailCreditosViewModel : ViewModel() {
    private val _creditos = MutableLiveData<CreditsResponse?>()
    val creditos: LiveData<CreditsResponse?> = _creditos

    fun fetchCreditos(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RemoteConnection.service.getCreditos(
                    movieId,
                    "49336a7ff05331f9880d3bc4f792f260"
                )
                _creditos.value = response
            } catch (e: Exception) {
                _creditos.value = null
            }
        }
    }
} 