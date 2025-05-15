package com.example.robertosanchez.watchit.db.PeliculaVista

import com.example.robertosanchez.watchit.db.Pelicula.Pelicula

data class PeliculaVista(
    val pelicula: Pelicula,
    val valoracion: Int,
    val review: String
)