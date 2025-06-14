package com.example.robertosanchez.watchinit.data.model

data class MediaItem(
    val id: Int,
    val poster: String,
    val release_date: String,
    val title: String,
    val sinopsis: String,
    val poster_fondo: String,
    val generos_ids: List<Int>
)
