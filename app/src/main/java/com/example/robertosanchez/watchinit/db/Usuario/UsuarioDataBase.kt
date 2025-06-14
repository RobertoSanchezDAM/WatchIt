package com.example.robertosanchez.watchinit.db.Usuario

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UsuarioDB::class], version = 1)
abstract class UsuarioDataBase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
}