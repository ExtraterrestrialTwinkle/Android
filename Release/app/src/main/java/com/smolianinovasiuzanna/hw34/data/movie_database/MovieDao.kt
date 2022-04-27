package com.smolianinovasiuzanna.hw34.data.movie_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun write(movieList: List<Movie>)

    @Query("SELECT * FROM ${MovieContract.TABLE_NAME} WHERE ${MovieContract.Columns.TITLE} LIKE :query AND ${MovieContract.Columns.TYPE} = :type ORDER BY ${MovieContract.Columns.TITLE} ASC")
    fun read(query: String, type: MovieType): Flow<List<Movie>>

    @Query("SELECT * FROM ${MovieContract.TABLE_NAME} ORDER BY ${MovieContract.Columns.TITLE} ASC")
    fun readAll(): Flow<List<Movie>>
}