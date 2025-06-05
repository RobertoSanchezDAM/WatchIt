package com.example.robertosanchez.watchit.repositories.models

data class Review(
    val id: String = "",
    val movieId: Int = 0,
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String? = null,
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) 