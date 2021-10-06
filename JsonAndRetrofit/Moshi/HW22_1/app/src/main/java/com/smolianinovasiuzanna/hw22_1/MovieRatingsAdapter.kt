package com.smolianinovasiuzanna.hw22_1

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

class MovieRatingsAdapter {
    @FromJson
    fun fromJson(movieRatings: MovieWithListOfScores): ImdbResponse.Movie {
        return ImdbResponse.Movie(
            id = movieRatings.id,
            title = movieRatings.title,
            year = movieRatings.year,
            genre = movieRatings.genre,
            rating = movieRatings.rating,
            posterLink = movieRatings.posterLink,
            scores = movieRatings.scores.map { rating ->
                rating.source to rating.score
            }.toMap()
        )
    }

    @ToJson
    fun toJson(movie: ImdbResponse.Movie): MovieWithListOfScores {
        return MovieWithListOfScores(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            genre = movie.genre,
            rating = movie.rating,
            posterLink = movie.posterLink,
            scores = movie.scores.map { (k, v) ->
                MovieScore(k, v)
            }.toList()
        )
    }

    @JsonClass(generateAdapter = true)
    data class MovieWithListOfScores(
        @Json(name = "Title") val title: String,
        @Json(name = "Year") val year: Int?,
        @Json(name = "Genre") val genre: String?,
        @Json(name = "Rated") val rating: MovieRating = MovieRating.G,
        @Json(name = "Poster") val posterLink: String?,
        @Json(name = "Ratings") val scores: List<MovieScore>,
        @Json(name = "imdbID") val id: String
    )
}

