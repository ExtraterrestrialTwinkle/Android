package com.smolianinovasiuzanna.hw27.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object Database {
    lateinit var instance: FactoryDatabase
        private set

    fun init(context: Context){
        instance = Room.databaseBuilder(context, FactoryDatabase::class.java, FactoryDatabase.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()

    }
}