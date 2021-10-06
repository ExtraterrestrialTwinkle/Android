package com.smolianinovasiuzanna.hw22_1

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class Repository(val errorCallback: (ImdbResponse.Error) -> Unit) {
    lateinit var movie: ImdbResponse.Movie
    fun searchMovie(
        title: String,
        callback: (List<ImdbResponse.Movie>) -> Unit
    ): Call {
        return Network.searchMovieCall(title).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                   errorCallback(ImdbResponse.Error(e))
                    Log.e("Error", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("Repo", "onResponse")
                    if (response.isSuccessful) {
                        Log.d("Repo", "parseMovieResponse response isSuccessful")
                            val responseString = response.body?.string().orEmpty()
                            Log.d("Server", "responseString = $responseString")
                            val movies = convertJsonToMovie(responseString)
                            callback(movies)
                    } else {
                        Log.d("Repo", "parseMovieResponse response is not Successful")
                        errorCallback(ImdbResponse.Error(Throwable("Response not Successful")))
                    }
                }
            })
        }
    }

    private fun convertJsonToMovie(responseBodyString: String): List<ImdbResponse.Movie> {

        val moshi = Moshi.Builder()
            .add(MovieRatingsAdapter())
            .build()
        val moshiAdapter = moshi.adapter(ImdbResponse.Movie::class.java).nonNull()
        return try {
            movie = moshiAdapter.fromJson(responseBodyString) as ImdbResponse.Movie
            listOf(movie)
        } catch (e: Exception) {
            errorCallback(ImdbResponse.Error(Throwable("Film not found")))
            emptyList()
        }
    }

    fun getScoreToMovie(source: String, score: String){
        val newMovieScores = mapOf(source to score)
        println(newMovieScores)
        val scores = (movie.scores.asSequence() + newMovieScores.asSequence()).distinct()
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.joinToString(",") }

        val newMovie = ImdbResponse.Movie(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            genre = movie.genre,
            rating = movie.rating,
            posterLink = movie.posterLink,
            scores = scores
        )
        val convertedMovie = convertMovieToJson(newMovie)
        Log.d("newMovie", "$convertedMovie")
    }

    private fun convertMovieToJson(movie: ImdbResponse.Movie){
        val moshi = Moshi.Builder()
            .add(MovieRatingsAdapter())
            .build()

        val moshiAdapter = moshi.adapter(ImdbResponse.Movie::class.java).nonNull()
        try {
            val movieJson = moshiAdapter.toJson(movie)
            Log.d("MovieJson", movieJson)
        } catch (e: Exception) {
            Log.e("Error convertMovieToJson", "${e.message}")
        }
    }
}