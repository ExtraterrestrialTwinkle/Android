package com.smolianinovasiuzanna.hw34.data.movie_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NestedObject(
    @Json( name = "Search")
    val search: List<Movie>
)

@Entity(
    tableName = MovieContract.TABLE_NAME,
    indices = [ Index(MovieContract.Columns.TITLE) ])
@JsonClass(generateAdapter = true)
data class Movie (
    @PrimaryKey
    @ColumnInfo( name = MovieContract.Columns.ID )
    @Json(name = "imdbID")
    val id: String,

    @ColumnInfo( name = MovieContract.Columns.TITLE )
    @Json(name = "Title")
    val title: String,

    @ColumnInfo( name = MovieContract.Columns.YEAR )
    @Json(name = "Year")
    val year: String,

    @ColumnInfo( name = MovieContract.Columns.POSTER )
    @Json(name = "Poster")
    val posterLink: String?,

    @ColumnInfo( name = MovieContract.Columns.TYPE )
    @Json(name = "Type")
    val type: MovieType
    )