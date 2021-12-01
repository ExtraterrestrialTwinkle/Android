package com.smolianinovasiuzanna.hw27.data.dao

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM ${EmployeesContract.TABLE_NAME}")
    suspend fun getAllEmployees(): List<Employee>

    @Query("SELECT * FROM ${EmployeesContract.TABLE_NAME} WHERE ${EmployeesContract.Columns.ID} =:employeeId")
    suspend fun getEmployeeById(employeeId: Int): Employee

    @Query("DELETE FROM ${EmployeesContract.TABLE_NAME} WHERE ${EmployeesContract.Columns.ID} =:employeeId")
    suspend fun deleteEmployee(employeeId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<Employee>)

    @Update
    suspend fun updateEmployee(employee: Employee)

    @Query("SELECT * FROM ${EmployeesContract.TABLE_NAME} WHERE ${EmployeesContract.Columns.LAST_NAME} LIKE :lastName")
    suspend fun getEmployeeByLastName(lastName: String): List<Employee>

}