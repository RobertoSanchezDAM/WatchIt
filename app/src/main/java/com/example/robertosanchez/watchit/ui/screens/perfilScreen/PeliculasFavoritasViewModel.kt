package com.example.robertosanchez.watchit.ui.screens.perfilScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeliculasFavoritasViewModel(
    private val firestore: FirestoreManager,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUserId = authManager.getCurrentUser()?.uid
            
            if (currentUserId != null) {
                try {
                    firestore.getFavoriteMovies(currentUserId).collect { peliculas ->
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

    fun addFavoriteMovie(movie: Pelicula): Boolean {
        val currentUserId = authManager.getCurrentUser()?.uid
        
        if (currentUserId == null) {
            return false
        }

        if (_uiState.value.peliculas.size >= 4) {
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
                firestore.addFavoriteMovie(currentUserId, movie)
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

    fun removeFavoriteMovie(movie: Pelicula) {
        val currentUserId = authManager.getCurrentUser()?.uid
        
        if (currentUserId != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    peliculas = currentState.peliculas.filter { it.peliculaId != movie.peliculaId }
                )
            }

            viewModelScope.launch {
                try {
                    firestore.removeFavoriteMovie(currentUserId, movie)
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

    fun isFavorite(movieId: Int): Boolean {
        return _uiState.value.peliculas.any { it.peliculaId == movieId }
    }

    fun canAddMoreFavorites(): Boolean {
        return _uiState.value.peliculas.size < 4
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val peliculas: List<Pelicula> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)
