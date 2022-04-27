package com.smolianinovasiuzanna.hw34.data.movie_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smolianinovasiuzanna.hw34.data.movie_database.MovieDatabase.Companion.DB_VERSION

@TypeConverters( MovieTypeConverter::class )
@Database( entities = [
    Movie::class
], version = DB_VERSION )
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object{
        const val DB_VERSION = 1
        const val DB_NAME = "factory_database"
    }
}