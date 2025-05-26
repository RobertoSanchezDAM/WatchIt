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

    // Peliculas
    fun getPeliculasTransactions(): Flow<List<Pelicula>> {
        return firestore.collection("pelicula")
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    peliculaDBToPelicula(ds.toObject(PeliculaDB::class.java), ds.id)
                }
            }
    }

    private fun peliculaDBToPelicula(peliculaDB: PeliculaDB?, idDocumento: String) =
        Pelicula(
            id = idDocumento,
            peliculaId = peliculaDB?.peliculaId ?: 0,
            poster = peliculaDB?.poster
        )


    suspend fun addPelicula(pelicula: Pelicula) {
        firestore.collection("pelicula").add(pelicula).await()
    }

    suspend fun deletePeliculaById(peliculaId: String) {
        firestore.collection("pelicula").document(peliculaId).delete().await()
    }

    // Usuarios
    suspend fun guardarUsuario(uid: String) {
        val userDocument = UsuarioDB(userId = uid)
        firestore.collection("usuarios").document(uid).set(userDocument).await()
    }
}

