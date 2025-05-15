package com.example.robertosanchez.watchit.ui.screens.perfilScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeliculasFavoritasViewModel(private val firestore: FirestoreManager) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            firestore.getPeliculasTransactions().collect { pelicula ->
                _uiState.update { uiState ->
                    uiState.copy(peliculas = pelicula, isLoading = false)
                }
            }
        }
    }

    // Peliculas
    fun addPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            firestore.addPelicula(pelicula)
            firestore.getPeliculasTransactions().collect { pelicula ->
                _uiState.update { uiState -> uiState.copy(peliculas = pelicula) }
            }
        }
    }

    fun deletePeliculaById(peliculaId: String) {
        if (peliculaId.isEmpty()) return
        viewModelScope.launch {
            firestore.deletePeliculaById(peliculaId)
            firestore.getPeliculasTransactions().collect { pelicula ->
                _uiState.update { uiState -> uiState.copy(peliculas = pelicula) }
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val peliculas: List<Pelicula> = emptyList(),
    val showAddNoteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)