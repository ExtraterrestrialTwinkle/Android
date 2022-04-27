package com.smolianinovasiuzanna.hw34.data.movie_database

import androidx.room.TypeConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class MovieType(name: String) {
    @Json( name = "movie") MOVIE ("movie"),
    @Json( name = "series")  SERIES ("series"),
    @Json( name = "episode") EPISODE("episode")
}

class MovieTypeConverter{
    @TypeConverter
    fun convertTypeToString(type: MovieType): String = type.name

    @TypeConverter
    fun convertStringToType(typeString: String): MovieType = MovieType.valueOf(typeString)
}