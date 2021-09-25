package com.smolianinovasiuzanna.hw21

import android.util.Log

//data class Movie(
//    val title: String,
//    val year: String,
//    val type: String,
//    val id: String,
//    val posterLink: String
//)

sealed class ImdbResponse {
    data class Movie(
        val title: String,
        val year: String,
        val type: String,
        val id: String,
        val posterLink: String
    ) : ImdbResponse()

    data class Error(val error: Throwable) : ImdbResponse() {
        override fun toString(): String {
            return "${error.message}"
        }
    }
}



