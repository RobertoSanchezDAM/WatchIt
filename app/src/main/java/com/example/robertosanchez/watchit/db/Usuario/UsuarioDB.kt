package com.example.robertosanchez.watchit.db.Usuario

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.db.PeliculaVer.PeliculaVer
import com.example.robertosanchez.watchit.db.PeliculaVista.PeliculaVista

@Entity
data class UsuarioDB(
    val userId: String = "",
    val peliculasVistas: List<PeliculaVista> = listOf(),
    val peliculasPorVer: List<PeliculaVer> = listOf(),
    val numPeliculasVistas: Int = 0,
    val valoracionMedia: Int = 0,
)

