package com.example.robertosanchez.watchit.repositories.models

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String,
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
) 