package com.smolianinovasiuzanna.hw34.data.movie_database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object Database {
    lateinit var instance: MovieDatabase
        private set

    fun init(context: Context){
        instance = Room.databaseBuilder(context, MovieDatabase::class.java, MovieDatabase.DB_NAME)
            .build()

    }
}