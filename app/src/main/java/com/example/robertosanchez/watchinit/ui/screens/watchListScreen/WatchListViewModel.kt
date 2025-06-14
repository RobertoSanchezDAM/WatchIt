package com.example.robertosanchez.watchinit.ui.screens.watchListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager
import com.example.robertosanchez.watchinit.db.Pelicula.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WatchListViewModel(
    private val firestore: FirestoreManager,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadWatchList()
    }

    fun loadWatchList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUserId = authManager.getCurrentUser()?.uid

            if (currentUserId != null) {
                try {
                    firestore.getWacthList(currentUserId).collect { peliculas ->
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
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun addWatchList(movie: Pelicula): Boolean {
        val currentUserId = authManager.getCurrentUser()?.uid

        if (currentUserId == null) {
            return false
        }

        if (_uiState.value.peliculas.any { it.peliculaId == movie.peliculaId }) {
            return false
        }

        _uiState.update { currentState ->
            currentState.copy(
                peliculas = currentState.peliculas + movie
            )
        }

        viewModelScope.launch {
            try {
                firestore.addWatchList(currentUserId, movie)
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        peliculas = currentState.peliculas.filter { it.peliculaId != movie.peliculaId }
                    )
                }
            }
        }
        return true
    }

    fun removeWatchList(movie: Pelicula) {
        val currentUserId = authManager.getCurrentUser()?.uid

        if (currentUserId != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    peliculas = currentState.peliculas.filter { it.peliculaId != movie.peliculaId }
                )
            }

            viewModelScope.launch {
                try {
                    firestore.removeWatchList(currentUserId, movie)
                } catch (e: Exception) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            peliculas = currentState.peliculas + movie
                        )
                    }
                }
            }
        }
    }

    fun isWatchList(movieId: Int): Boolean {
        return _uiState.value.peliculas.any { it.peliculaId == movieId }
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val peliculas: List<Pelicula> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

