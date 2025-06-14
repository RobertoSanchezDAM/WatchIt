package com.example.robertosanchez.watchinit.db

import android.content.Context
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchinit.db.PeliculasVistas.PeliculasVistas
import com.example.robertosanchez.watchinit.db.Usuario.UsuarioDB
import com.example.robertosanchez.watchinit.repositories.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    // Usuarios
    suspend fun guardarUsuario(uid: String, username: String = "") {
        val userDocument = UsuarioDB(userId = uid, username = username)
        firestore.collection("usuarios").document(uid).set(userDocument).await()
    }

    // Peliculas Favoritas (Subcoleccion "peliculas_favoritas")
    suspend fun addPeliculaFavorita(userId: String, movie: Pelicula) {
        firestore.collection("usuarios").document(userId).collection("peliculas_favoritas").document(movie.peliculaId.toString()).set(movie).await()
    }

    suspend fun removePeliculaFavorita(userId: String, movie: Pelicula) {
        firestore.collection("usuarios").document(userId).collection("peliculas_favoritas").document(movie.peliculaId.toString()).delete().await()
    }

    fun getFavoriteMovies(userId: String): Flow<List<Pelicula>> {
        return firestore.collection("usuarios").document(userId).collection("peliculas_favoritas")
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject(Pelicula::class.java) }
            }
    }

    // Watch List (Subcoleccion "watch_list")
    suspend fun addWatchList(userId: String, movie: Pelicula) {
        firestore.collection("usuarios").document(userId).collection("watch_list").document(movie.peliculaId.toString()).set(movie).await()
    }

    suspend fun removeWatchList(userId: String, movie: Pelicula) {
        firestore.collection("usuarios").document(userId).collection("watch_list").document(movie.peliculaId.toString()).delete().await()
    }

    fun getWacthList(userId: String): Flow<List<Pelicula>> {
        return firestore.collection("usuarios").document(userId).collection("watch_list")
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject(Pelicula::class.java) }
            }
    }

    // Peliculas Vistas (Subcoleccion "peliculas_vistas")
    suspend fun addVistas(userId: String, movie: PeliculasVistas) {
        firestore.collection("usuarios").document(userId).collection("peliculas_vistas").document(movie.peliculaId.toString()).set(movie).await()
    }

    suspend fun removeVistas(userId: String, movie: PeliculasVistas) {
        firestore.collection("usuarios").document(userId).collection("peliculas_vistas").document(movie.peliculaId.toString()).delete().await()
    }

    fun getVistas(userId: String): Flow<List<PeliculasVistas>> {
        return firestore.collection("usuarios").document(userId).collection("peliculas_vistas")
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject(PeliculasVistas::class.java) }
            }
    }

    suspend fun updateVistaRating(userId: String, movieId: Int, rating: Int) {
        firestore.collection("usuarios")
            .document(userId)
            .collection("peliculas_vistas")
            .document(movieId.toString())
            .update("estrellas", rating)
            .await()
    }

    // Reviews
    fun getUserReviews(userId: String): Flow<List<Review>> {
        return firestore.collection("reviews")
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.copy(id = doc.id)
                }.sortedByDescending { it.timestamp }
            }
    }
}

