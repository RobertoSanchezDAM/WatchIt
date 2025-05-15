package com.example.robertosanchez.watchit.db.Pelicula

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PeliculaDB(
    val id: String = "",
    val peliculaId: Int = 0,
    val poster: String = ""
)