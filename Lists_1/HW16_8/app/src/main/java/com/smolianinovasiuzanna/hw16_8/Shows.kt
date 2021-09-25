package com.smolianinovasiuzanna.hw16_8

import android.os.Parcelable

sealed class Shows: Parcelable {
    @kotlinx.parcelize.Parcelize
    data class Film(
        val title: String,
        val year: String,
        val posterLink: String,
        val director: String
    ): Shows()
    @kotlinx.parcelize.Parcelize
    data class Series(
        val title: String,
        val seasons: String,
        val posterLink: String,
        val creators: String,

    ): Shows()
}