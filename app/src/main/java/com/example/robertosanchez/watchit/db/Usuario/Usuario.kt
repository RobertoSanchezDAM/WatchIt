package com.example.robertosanchez.watchit.db.Usuario

import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.db.PeliculaVer.PeliculaVer
import com.example.robertosanchez.watchit.db.PeliculaVista.PeliculaVista

data class Usuario(
    val user_id: String,
    val peliculasVistas: MutableList<PeliculaVista>,
    val peliculasPorVer: MutableList<PeliculaVer>,
    val numPeliculasVistas: Int,
    val valoracionMedia: Int,
)
