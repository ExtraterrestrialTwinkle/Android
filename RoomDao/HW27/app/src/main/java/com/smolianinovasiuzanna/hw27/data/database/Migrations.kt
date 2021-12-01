package com.smolianinovasiuzanna.hw27.data.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Log.d("Migrations","migration 1-2 start")
        //Не применяем константы, т.к. в какой-либо версии название столбцов может измениться
        database.execSQL("ALTER TABLE employees ADD COLUMN experience INTEGER NOT NULL DEFAULT 0")
        Log.d("Migrations","migration 1-2 success")
    }
}