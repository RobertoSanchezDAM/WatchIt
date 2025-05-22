package com.example.robertosanchez.watchit.repositories

import com.example.robertosanchez.watchit.repositories.models.CreditsResponse
import com.example.robertosanchez.watchit.repositories.models.MovieImagesResponse
import com.example.robertosanchez.watchit.repositories.models.RemoteResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET("discover/movie?language=es-ES&sort_by=popularity.desc")
    suspend fun peliculasPopulares(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("discover/movie?language=es-ES&sort_by=vote_count.desc")
    suspend fun peliculasRated(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): RemoteResult

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

    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): RemoteResult

    @GET("discover/movie")
    suspend fun peliculaFecha (
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sort_by: String = "popularity.desc",
        @Query("primary_release_year") year: Int,
    ): RemoteResult

    @GET("discover/movie")
    suspend fun peliculaGenero (
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sort_by: String = "popularity.desc",
        @Query("with_genres") genre: Int,
    ): RemoteResult

    @GET("movie/upcoming")
    suspend fun proximosEstrenos(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): RemoteResult

    @GET("movie/now_playing")
    suspend fun peliculasEnCine(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("region") region: String = "ES",
        @Query("page") page: Int = 1
    ): RemoteResult
}