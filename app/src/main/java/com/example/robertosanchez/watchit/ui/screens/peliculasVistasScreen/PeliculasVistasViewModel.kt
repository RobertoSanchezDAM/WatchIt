package com.example.robertosanchez.watchit.ui.screens.peliculasVistasScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.db.PeliculasVistas.PeliculasVistas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeliculasVistasViewModel (
    private val firestore: FirestoreManager,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private var allPeliculas = listOf<PeliculasVistas>()

    init {
        loadVistas()
    }

    fun loadVistas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUserId = authManager.getCurrentUser()?.uid

            if (currentUserId != null) {
                try {
                    firestore.getVistas(currentUserId).collect { peliculas ->
                        allPeliculas = peliculas
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

    fun addVista(movie: PeliculasVistas): Boolean {
        val currentUserId = authManager.getCurrentUser()?.uid

        if (currentUserId == null) {
            return false
        }

        if (allPeliculas.any { it.peliculaId == movie.peliculaId }) {
            return false
        }

        viewModelScope.launch {
            try {
                firestore.addVistas(currentUserId, movie)
                // El contador se actualizará automáticamente cuando se actualice la lista
            } catch (e: Exception) {
                // Manejar el error si es necesario
            }
        }
        return true
    }

    fun removeVista(movie: PeliculasVistas) {
        val currentUserId = authManager.getCurrentUser()?.uid

        if (currentUserId != null) {
            viewModelScope.launch {
                try {
                    firestore.removeVistas(currentUserId, movie)
                    // El contador se actualizará automáticamente cuando se actualice la lista
                } catch (e: Exception) {
                    // Manejar el error si es necesario
                }
            }
        }
    }

    fun isVista(movieId: Int): Boolean {
        return _uiState.value.peliculas.any { it.peliculaId == movieId }
    }

    fun getRating(movieId: Int): Int {
        return _uiState.value.peliculas.find { it.peliculaId == movieId }?.estrellas ?: 0
    }

    fun updateRating(movieId: Int, rating: Int) {
        val currentUserId = authManager.getCurrentUser()?.uid

        if (currentUserId != null) {
            viewModelScope.launch {
                try {
                    firestore.updateVistaRating(currentUserId, movieId, rating)
                    _uiState.update { currentState ->
                        currentState.copy(
                            peliculas = currentState.peliculas.map { pelicula ->
                                if (pelicula.peliculaId == movieId) {
                                    pelicula.copy(estrellas = rating)
                                } else {
                                    pelicula
                                }
                            }
                        )
                    }
                } catch (e: Exception) {
                    // Manejar el error si es necesario
                }
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val peliculas: List<PeliculasVistas> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

