package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectWithTasks

@Dao
interface ProjectDao {

    @Query("SELECT * FROM ${ProjectContract.TABLE_NAME}")
    suspend fun getAllProjects(): List<Project>

    @Query("SELECT * FROM ${ProjectContract.TABLE_NAME} WHERE ${ProjectContract.Columns.ID} =:projectId")
    suspend fun getProjectById(projectId: Int): Project

    @Query("DELETE FROM ${ProjectContract.TABLE_NAME} WHERE ${ProjectContract.Columns.ID} =:projectId")
    suspend fun deleteProject(projectId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Update
    suspend fun updateProject(project: Project)

    @Transaction
    @Query("SELECT * FROM ${ProjectContract.TABLE_NAME} WHERE ${ProjectContract.Columns.ID} =:projectId")
    suspend fun getProjectsTasks(projectId: Int): ProjectWithTasks

}