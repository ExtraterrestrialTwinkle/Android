package com.smolianinovasiuzanna.hw22_1

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieScore (
    @Json(name = "Source")
    val source: String,
    @Json(name = "Value")
    val score: String
    )