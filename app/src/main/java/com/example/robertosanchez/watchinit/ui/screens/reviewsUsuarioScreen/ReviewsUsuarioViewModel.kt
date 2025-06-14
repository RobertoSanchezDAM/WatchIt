package com.example.robertosanchez.watchinit.ui.screens.reviewsUsuarioScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager
import com.example.robertosanchez.watchinit.repositories.models.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewsUsuarioViewModel(
    private val firestore: FirestoreManager,
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadReviews()
    }

    fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUserId = authManager.getCurrentUser()?.uid
            
            if (currentUserId != null) {
                try {
                    firestore.getUserReviews(currentUserId).collect { reviews ->
                        _uiState.update { uiState -> 
                            uiState.copy(
                                reviews = reviews,
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

    fun loadReviewsUsuario(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                firestore.getUserReviews(userId).collect { reviews ->
                    _uiState.update { uiState -> 
                        uiState.copy(
                            reviews = reviews,
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
    val reviews: List<Review> = emptyList()
) 