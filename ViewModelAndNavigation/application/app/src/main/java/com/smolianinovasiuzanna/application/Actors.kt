package com.smolianinovasiuzanna.application

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Actors (
    val id: Int,
    val name: String,
    val photoLink: String
) : Parcelable