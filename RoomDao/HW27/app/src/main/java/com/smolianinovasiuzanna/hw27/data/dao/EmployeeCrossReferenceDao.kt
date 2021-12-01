package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesWithTasks
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract
import com.smolianinovasiuzanna.hw27.data.entities.task.TasksWithEmployees

@Dao
interface EmployeeCrossReferenceDao {

    @Transaction
    @Query("SELECT * FROM  ${EmployeesContract.TABLE_NAME} WHERE ${EmployeesContract.Columns.ID} =:employeeId")
    suspend fun getEmployeeWithTasks(employeeId: Int): EmployeesWithTasks

    @Transaction
    @Query("SELECT * FROM  ${TaskContract.TABLE_NAME} WHERE ${TaskContract.Columns.ID} =:taskId")
    suspend fun getTaskWithEmployees(taskId: Int): TasksWithEmployees

    @Transaction
    @Insert
    suspend fun insertEmployeeWithTask(references: List<EmployeesCrossReferences>): List<Long>

    @Transaction
    @Query("DELETE FROM EmployeesCrossReferences WHERE ${EmployeesContract.Columns.ID} =:employeeId AND ${TaskContract.Columns.ID} =:taskId")
    suspend fun removeRelationBetweenTaskAndEmployee(employeeId: Int, taskId: Int)
}