package com.example.robertosanchez.watchit.data.model

data class Peliculas(
    val id: Int,
    val poster: String,
    val release_date: String,
    val title: String,
    val sinopsis: String,
    val poster_fondo: String,
    val genre_ids: List<Int>
)
