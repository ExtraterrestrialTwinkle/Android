package com.smolianinovasiuzanna.hw34.data.networking

import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import com.smolianinovasiuzanna.hw34.data.movie_database.MovieType
import com.smolianinovasiuzanna.hw34.data.movie_database.NestedObject
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("/")
    fun searchMovie(
        @Query("s") query: String,
        @Query("type") type: MovieType
    ): Call<NestedObject>
}