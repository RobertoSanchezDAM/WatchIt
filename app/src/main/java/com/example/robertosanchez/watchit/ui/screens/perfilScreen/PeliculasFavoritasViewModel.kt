package com.example.robertosanchez.watchit.ui.screens.perfilScreen

import androidx.lifecycle.ViewModel
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

    fun addPeliculaFavorita(movie: Pelicula): Boolean {
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
                firestore.addPeliculaFavorita(currentUserId, movie)
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

    fun removePeliculaFavorita(movie: Pelicula) {
        val currentUserId = authManager.getCurrentUser()?.uid
        
        if (currentUserId != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    peliculas = currentState.peliculas.filter { it.peliculaId != movie.peliculaId }
                )
            }

            viewModelScope.launch {
                try {
                    firestore.removePeliculaFavorita(currentUserId, movie)
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

    fun isFavorita(movieId: Int): Boolean {
        return _uiState.value.peliculas.any { it.peliculaId == movieId }
    }

    fun puedoAnadirMasFavoritas(): Boolean {
        return _uiState.value.peliculas.size < 4
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val peliculas: List<Pelicula> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)
