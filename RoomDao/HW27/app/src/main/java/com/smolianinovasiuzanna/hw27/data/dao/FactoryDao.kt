package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.factory.Factory

@Dao
interface FactoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFactories(factories: List<Factory>)
}