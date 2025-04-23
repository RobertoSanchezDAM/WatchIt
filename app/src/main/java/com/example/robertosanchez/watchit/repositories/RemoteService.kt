package com.example.robertosanchez.proyectoapi.repositories


import com.example.robertosanchez.watchit.repositories.models.RemoteResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET ("discover/movie?language=es-ES&sort_by=popularity.desc")
    suspend fun popularMovies(@Query("api_key")apiKey: String): RemoteResult

    @GET ("discover/movie?language=es-ES&sort_by=vote_count.desc")
    suspend fun ratedMovies(@Query("api_key")apiKey: String): RemoteResult
}