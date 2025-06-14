package com.example.robertosanchez.watchinit.repositories.models

data class MovieImagesResponse(
    val backdrops: List<MovieImage>,
    val posters: List<MovieImage>
)

data class MovieImage(
    val file_path: String,
    val width: Int?,
    val height: Int?,
    val aspect_ratio: Float?,
    val iso_639_1: String?,
    val vote_average: Float?,
    val vote_count: Int?
) 