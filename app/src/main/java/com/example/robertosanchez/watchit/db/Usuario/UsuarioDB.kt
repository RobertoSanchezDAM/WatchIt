package com.example.robertosanchez.watchit.db.Usuario

import androidx.room.Entity

@Entity
data class UsuarioDB(
    val userId: String = "",
    val valoracionMedia: Int = 0,
)

