package com.smolianinovasiuzanna.hw22_1

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.StringBuilder

sealed class ImdbResponse {

    @JsonClass(generateAdapter = true)
    data class Movie(
        @Json(name = "Title") val title: String,
        @Json(name = "Year") val year: Int?,
        @Json(name = "Genre") val genre: String?,
        @Json(name = "Rated") val rating: MovieRating = MovieRating.G,
        @Json(name = "Poster") val posterLink: String?,
        @Json(name = "Ratings") val scores: Map<String, String> = emptyMap(),
        @Json(name = "imdbID") val id: String
    )


    data class Error(val error: Throwable) {
        override fun toString(): String {
            return "${error.message}"
        }
    }
}



