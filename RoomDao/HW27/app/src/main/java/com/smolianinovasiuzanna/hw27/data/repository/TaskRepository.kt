package com.smolianinovasiuzanna.hw27.data.repository

import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract

class TaskRepository {

    private val taskDao = Database.instance.taskDao()
    private val employeesCrossReferenceDao = Database.instance.employeeCrossReferenceDao()

    suspend fun getAllTasks(): List<Task>{
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(taskId: Int): Task{
        return taskDao.getTaskById(taskId)
    }

    suspend fun deleteTask(taskId: Int){
        return taskDao.deleteTask(taskId)
    }

    suspend fun saveTask(task: Task){
        taskDao.insertTasks(listOf(task))
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun saveTaskWithEmployees(reference: EmployeesCrossReferences){
        Log.d("EmployeeRepository", "saveTaskWithEmployees")
        val list = employeesCrossReferenceDao.insertEmployeeWithTask(listOf(reference))
        Log.d("EmployeeRepository", "aveTaskWithEmployees after insertion. id = $list ")
    }

    suspend fun getTaskWithEmployees(taskId: Int): List<Employee> {
        Log.d("TaskRepository", "getTaskWithEmployees taskID = $taskId")
        val listTaskWithRelations = employeesCrossReferenceDao.getTaskWithEmployees(taskId)
        Log.d("TaskRepo getTaskWithEmp", "listTaskWithRelations = $listTaskWithRelations")
        val employees = listTaskWithRelations.employees
        Log.d("TaskRepo getTaskWithEmp", "employee = $employees")
        return employees
    }

    suspend fun removeRelationBetweenTaskAndEmployee(employeeId: Int, taskId: Int){
        Log.d("TaskRepo", "RemoveRelation")
        employeesCrossReferenceDao.removeRelationBetweenTaskAndEmployee(employeeId, taskId)

    }
}