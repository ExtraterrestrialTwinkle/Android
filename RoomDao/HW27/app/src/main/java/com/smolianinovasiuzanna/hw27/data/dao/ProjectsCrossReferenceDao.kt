package com.smolianinovasiuzanna.hw27.data.dao

import android.util.Log
import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.department.DepartmentContract
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectWithDepartments
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract

@Dao
interface ProjectsCrossReferenceDao {

    @Transaction
    @Query("SELECT * FROM ${ProjectContract.TABLE_NAME} WHERE ${ProjectContract.Columns.ID} =:projectId")
    suspend fun getProjectWithDepartments(projectId: Int): ProjectWithDepartments


    @Transaction
    @Insert
    suspend fun insertProjectWithDepartment(references: List<ProjectsCrossReferences>): List<Long>

    @Transaction
    @Query("DELETE FROM ProjectsCrossReferences WHERE ${ProjectContract.Columns.ID} =:projectId AND ${DepartmentContract.Columns.ID} =:departmentId")
    suspend fun removeProjectWithDepartment(projectId: Int, departmentId: Int)

    @Transaction
    @Query("DELETE FROM ProjectsCrossReferences WHERE ${ProjectContract.Columns.ID} =:projectId ")
    suspend fun removeAllRelationsOfProject(projectId: Int)
}