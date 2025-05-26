package com.example.robertosanchez.watchit.db

import android.content.Context
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.db.Pelicula.PeliculaDB
import com.example.robertosanchez.watchit.db.Usuario.UsuarioDB
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    // Peliculas (colección principal "peliculas")
    fun getPeliculasTransactions(): Flow<List<Pelicula>> {
        return firestore.collection("peliculas")
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    // Asumiendo que PeliculaDB tiene los campos necesarios para mapear a Pelicula
                    // y que el ID del documento de Firestore es el ID de la película.
                    ds.toObject(PeliculaDB::class.java)?.let { peliculaDB ->
                         Pelicula(
                             id = ds.id,
                             peliculaId = peliculaDB.peliculaId,
                             poster = peliculaDB.poster
                         )
                    }
                }
            }
    }

    private fun peliculaDBToPelicula(peliculaDB: PeliculaDB?, idDocumento: String) = // Mantener esta función si se usa en otro lugar
        Pelicula(
            id = idDocumento,
            peliculaId = peliculaDB?.peliculaId ?: 0,
            poster = peliculaDB?.poster
        )


    suspend fun addPelicula(pelicula: Pelicula) {
        // Podrías querer usar pelicula.id si ya viene con un ID, o dejar que Firestore genere uno
        // Si pelicula.id proviene de una API, asegúrate de que sea un ID de documento válido
        firestore.collection("peliculas").document(pelicula.id).set(pelicula).await()
    }

    suspend fun deletePeliculaById(peliculaId: String) {
        firestore.collection("peliculas").document(peliculaId).delete().await()
    }

    // Usuarios
    suspend fun guardarUsuario(uid: String) {
        val userDocument = UsuarioDB(userId = uid)
        firestore.collection("usuarios").document(uid).set(userDocument).await()
    }

    // Peliculas Favoritas (Subcoleccion "peliculas_favoritas")
    suspend fun addFavoriteMovie(userId: String, movie: Pelicula) {
        // Usar peliculaId.toString() como ID del documento en la subcolección
        firestore.collection("usuarios").document(userId).collection("peliculas_favoritas").document(movie.peliculaId.toString()).set(movie).await()
    }

    suspend fun removeFavoriteMovie(userId: String, movie: Pelicula) {
        // Usar peliculaId.toString() como ID del documento en la subcolección
        firestore.collection("usuarios").document(userId).collection("peliculas_favoritas").document(movie.peliculaId.toString()).delete().await()
    }

    fun getFavoriteMovies(userId: String): Flow<List<Pelicula>> {
        return firestore.collection("usuarios").document(userId).collection("peliculas_favoritas")
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject(Pelicula::class.java) }
            }
    }
}

