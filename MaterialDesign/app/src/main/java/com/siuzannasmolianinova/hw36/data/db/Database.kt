package com.siuzannasmolianinova.hw36.data.db

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: ArticlesDatabase
        private set

    fun init(context: Context){
        instance = Room.databaseBuilder(context, ArticlesDatabase::class.java, ArticlesDatabase.DB_NAME)
            .build()

    }
}