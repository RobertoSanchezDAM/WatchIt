package com.example.robertosanchez.watchinit.db.Usuario

import androidx.room.Entity

@Entity
data class UsuarioDB(
    val userId: String = "",
    val username: String = "",
    val valoracionMedia: Int = 0,
)

