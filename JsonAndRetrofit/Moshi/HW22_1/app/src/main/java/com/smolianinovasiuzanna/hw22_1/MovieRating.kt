package com.smolianinovasiuzanna.hw22_1

import com.squareup.moshi.Json

enum class MovieRating (val ageRating: String?){
    @Json(name = "G") G ("General audiences"),
    @Json(name = "PG") PG("Parental guidance suggested"),
    @Json(name = "PG-13") PG_13 ("Parents strongly cautioned"),
    @Json(name = "R") R ("Restricted"),
    @Json(name = "NC-17") NC_17 ("No one 17 & younger admitted"),
    @Json(name = "N/A") N_A ("No data")
}
