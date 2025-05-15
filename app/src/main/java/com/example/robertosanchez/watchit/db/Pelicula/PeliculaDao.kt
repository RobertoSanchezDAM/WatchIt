package com.example.robertosanchez.watchit.db.Pelicula

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeliculaDao {
    @Insert
    suspend fun insert(pelicula: Pelicula)

}