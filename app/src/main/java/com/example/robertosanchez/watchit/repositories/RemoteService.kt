package com.example.robertosanchez.proyectoapi.repositories


import com.example.robertosanchez.watchit.repositories.models.CreditsResponse
import com.example.robertosanchez.watchit.repositories.models.MovieImagesResponse
import com.example.robertosanchez.watchit.repositories.models.RemoteResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET ("discover/movie?language=es-ES&sort_by=popularity.desc")
    suspend fun popularMovies(@Query("api_key")apiKey: String): RemoteResult

    @GET ("discover/movie?language=es-ES&sort_by=vote_count.desc")
    suspend fun ratedMovies(@Query("api_key")apiKey: String): RemoteResult

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponse

    @GET("movie/{movie_id}/images")
    suspend fun getImages(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieImagesResponse
}