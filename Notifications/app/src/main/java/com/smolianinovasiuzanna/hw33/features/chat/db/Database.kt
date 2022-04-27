package com.smolianinovasiuzanna.hw33.features.chat.db

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: ChatDatabase
        private set

    fun init(context: Context){
        instance = Room.databaseBuilder(context, ChatDatabase::class.java, ChatDatabase.DB_NAME)
            .build()
    }
}