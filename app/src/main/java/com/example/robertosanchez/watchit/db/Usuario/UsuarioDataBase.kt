package com.example.robertosanchez.watchit.db.Usuario

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UsuarioDB::class], version = 1)
abstract class UsuarioDataBase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
}