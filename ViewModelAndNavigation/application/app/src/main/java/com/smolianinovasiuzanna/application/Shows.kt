package com.smolianinovasiuzanna.application

import android.os.Parcelable

sealed class Shows: Parcelable {
    @kotlinx.parcelize.Parcelize
    data class Film(
        val id: Long,
        val title: String,
        val year: String,
        val posterLink: String,
        val director: String
    ): Shows()
    @kotlinx.parcelize.Parcelize
    data class Series(
        val id: Long,
        val title: String,
        val seasons: String,
        val posterLink: String,
        val creators: String,

        ): Shows()
}

enum class Type{
    TYPE_FILM,
    TYPE_SERIES
}