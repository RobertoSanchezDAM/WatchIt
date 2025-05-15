package com.example.robertosanchez.watchit.db.Pelicula

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PeliculaDB::class], version = 1)
abstract class PeliculaDataBase : RoomDatabase() {
    abstract fun peliculaDao(): PeliculaDao
}

