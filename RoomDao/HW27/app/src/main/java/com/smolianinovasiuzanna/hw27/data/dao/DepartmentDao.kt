package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.department.DepartmentContract

@Dao
interface DepartmentDao {

    @Query("SELECT * FROM ${DepartmentContract.TABLE_NAME}")
    suspend fun getAllDepartments(): List<Department>

    @Query("SELECT * FROM ${DepartmentContract.TABLE_NAME} WHERE ${DepartmentContract.Columns.ID} =:departmentId")
    suspend fun getDepartmentById(departmentId: Int): Department

    @Query("DELETE FROM ${DepartmentContract.TABLE_NAME} WHERE ${DepartmentContract.Columns.ID} =:departmentId")
    suspend fun deleteDepartment(departmentId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<Department>)


}