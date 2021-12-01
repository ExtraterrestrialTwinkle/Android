package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract

@Dao
interface TaskDao {

    @Query("SELECT * FROM ${TaskContract.TABLE_NAME}")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM ${TaskContract.TABLE_NAME} WHERE ${TaskContract.Columns.ID} =:taskId")
    suspend fun getTaskById(taskId: Int): Task

    @Query("DELETE FROM ${TaskContract.TABLE_NAME} WHERE ${TaskContract.Columns.ID} =:taskId")
    suspend fun deleteTask(taskId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)
}