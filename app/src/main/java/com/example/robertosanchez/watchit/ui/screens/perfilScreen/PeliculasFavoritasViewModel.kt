package com.example.robertosanchez.watchit.ui.screens.perfilScreen

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

class PeliculasFavoritasViewModel(private val firestore: FirestoreManager, private val authManager: AuthManager) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authManager.getCurrentUser()?.uid?.let { userId ->
                firestore.getFavoriteMovies(userId).collect {
                    _uiState.update { uiState -> uiState.copy(peliculas = it, isLoading = false) }
                }
            }
        }
    }

    fun addFavoriteMovie(movie: Pelicula): Boolean {
        return if (_uiState.value.peliculas.size < 4) {
            viewModelScope.launch {
                authManager.getCurrentUser()?.uid?.let { userId ->
                    firestore.addFavoriteMovie(userId, movie)
                }
            }
            true
        } else {
            false
        }
    }

    fun removeFavoriteMovie(movie: Pelicula) {
        viewModelScope.launch {
            authManager.getCurrentUser()?.uid?.let { userId ->
                firestore.removeFavoriteMovie(userId, movie)
            }
        }
    }

    fun isFavorite(movieId: String): StateFlow<Boolean> {
        val isFav = MutableStateFlow(false)
        viewModelScope.launch {
            _uiState.collect { uiState ->
                isFav.value = uiState.peliculas.any { it.peliculaId.toString() == movieId }
            }
        }
        return isFav
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
