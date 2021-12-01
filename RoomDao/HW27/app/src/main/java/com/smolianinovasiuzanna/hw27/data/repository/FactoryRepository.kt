package com.smolianinovasiuzanna.hw27.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.factory.Factory

class FactoryRepository(context: Context) {

    private val factoryDao = Database.instance.factoryDao()
    private val sharedPrefs = context.getSharedPreferences("first_time_launch", Context.MODE_PRIVATE)

    suspend fun checkFirstTimeLaunch(context: Context){
        if(sharedPrefs.getBoolean("first_time", true)){
            Log.d("first time launch", "")
            try{
                saveFactory()
            } catch(t: Throwable){
                Log.e("FactoryRepo", "Error firstTimeLaunch", t)
            }
        }
        sharedPrefs.edit()
            .putBoolean("first_time", false)
            .apply()
    }
    private suspend fun saveFactory(){
        val factory = Factory(id = 1, title = "Завод")
        factoryDao.insertFactories(listOf(factory))
    }
}