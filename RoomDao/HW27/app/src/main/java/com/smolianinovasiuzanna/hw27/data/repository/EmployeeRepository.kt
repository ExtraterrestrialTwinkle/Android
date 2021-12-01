package com.smolianinovasiuzanna.hw27.data.repository

import android.util.Log
import com.smolianinovasiuzanna.hw27.data.IncorrectFormException
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesWithTasks
import com.smolianinovasiuzanna.hw27.data.entities.task.Task

class EmployeeRepository {

    private val employeeDao = Database.instance.employeeDao()
    private val employeesCrossReferenceDao = Database.instance.employeeCrossReferenceDao()

    suspend fun getAllEmployees(): List<Employee>{
        Log.d("EmployeeRepo", "getAllEmployees")
        return employeeDao.getAllEmployees()
    }

    suspend fun getEmployeeById(employeeId: Int): Employee{
        return employeeDao.getEmployeeById(employeeId)
    }

    suspend fun deleteEmployee(employeeId: Int){
        return employeeDao.deleteEmployee(employeeId)
    }

    suspend fun saveEmployees(employee: Employee){
        if (isEmployeeValid(employee).not()) throw IncorrectFormException()
        employeeDao.insertEmployees(listOf(employee))
    }

    suspend fun updateEmployee(employee: Employee){
        if (isEmployeeValid(employee).not()) throw IncorrectFormException()
        employeeDao.updateEmployee(employee)
    }

    suspend fun getEmployeeWithTasks(employeeId: Int): List<Task?> {
        Log.d("EmployeeRepository", "getEmployeeWithTasks employeeID = $employeeId")
        val listEmployeeWithRelations = employeesCrossReferenceDao.getEmployeeWithTasks(employeeId)
        Log.d("EmplRepo getEmplWithTas", "listEmployeeWithRelations = $listEmployeeWithRelations")
        val tasks = listEmployeeWithRelations.tasks
        Log.d("EmplRepo getEmplWithTas", "tasks = $tasks")
        return tasks
    }

    suspend fun getEmployeeByLastName(lastName: String): List<Employee>{
        Log.d("EmployeeRepo", "getEmployeeByLastName")
        return employeeDao.getEmployeeByLastName(lastName)
    }

    private fun isEmployeeValid(employee: Employee): Boolean {
        return employee.firstName.isNotBlank() &&
                employee.lastName.isNotBlank()
    }
}