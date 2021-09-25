package com.smolianinovasiuzanna.hw17

import android.graphics.drawable.Drawable
import android.media.Image
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

@kotlinx.parcelize.Parcelize
data class Posters (
    val id: Int,
    val title: String,
    val posterLink: String
) : Parcelable

@kotlinx.parcelize.Parcelize
data class Actors (
    val id: Int,
    val name: String,
    val photoLink: String
) : Parcelable

@kotlinx.parcelize.Parcelize
data class Pictures (
    val id: Int,
    val pictureLink: String
):Parcelable