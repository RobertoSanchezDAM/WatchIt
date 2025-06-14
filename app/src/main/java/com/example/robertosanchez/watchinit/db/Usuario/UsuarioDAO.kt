package com.example.robertosanchez.watchinit.db.Usuario

import androidx.room.Dao
import androidx.room.Insert
import com.example.robertosanchez.watchinit.db.Pelicula.Pelicula

@Dao
interface UsuarioDAO {
    @Insert
    fun agregarPeliculaFavorita(usuarioId: String, pelicula: Pelicula)
    fun quitarPeliculaFavorita(usuarioId: String, peliculaId: String)
    fun obtenerPeliculasFavoritas(usuarioId: String): List<Pelicula>

}