package com.example.robertosanchez.watchit.ui.screens.perfilUsuarioSelectScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilUsuarioSelectViewModel(
    private val firestore: FirestoreManager,
    private val userId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                firestore.getFavoriteMovies(userId).collect { peliculas ->
                    _uiState.update { uiState -> 
                        uiState.copy(
                            peliculas = peliculas.take(4),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val peliculas: List<Pelicula> = emptyList()
) 