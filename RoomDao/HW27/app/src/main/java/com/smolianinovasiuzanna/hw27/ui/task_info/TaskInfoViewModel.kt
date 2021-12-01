package com.smolianinovasiuzanna.hw27.ui.task_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.repository.EmployeeRepository
import com.smolianinovasiuzanna.hw27.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskInfoViewModel : ViewModel() {

    private val taskRepository = TaskRepository()
    private val employeeRepository = EmployeeRepository()

    private val addedEmployeeLiveData = MutableLiveData<List<Employee>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val addedEmployees: LiveData<List<Employee>>
        get() = addedEmployeeLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun getAddedEmployeeListByTaskId(taskId: Int){
        try{
            viewModelScope.launch {
                addedEmployeeLiveData.postValue(taskRepository.getTaskWithEmployees(taskId))
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
            addedEmployeeLiveData.postValue(emptyList())
        }
    }

    fun getEmployeesSearchListByLastName(lastName: String?){
        val lastNameForQuery = "$lastName%"
        Log.d("Search", "lastName = $lastName, lastNameForQuery = $lastNameForQuery")
        try{
            viewModelScope.launch {
                addedEmployeeLiveData.postValue(employeeRepository.getEmployeeByLastName(lastNameForQuery))
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
            addedEmployeeLiveData.postValue(emptyList())
        }
    }

    fun getAllEmployees() {
        Log.d("Search", "getAllEmployees")
        try {
            viewModelScope.launch {
               addedEmployeeLiveData. postValue(employeeRepository.getAllEmployees())
            }

        } catch (t: Throwable) {
            errorLiveData.postValue(t)
           addedEmployeeLiveData.postValue(emptyList())
        }
    }

    fun removeRelation(taskId: Int, employeeId: Int){
        try{
            viewModelScope.launch {
                Database.instance.withTransaction {
                    val employee = employeeRepository.getEmployeeById(employeeId)
                    if(taskRepository.getTaskWithEmployees(taskId).contains(employee)){
                        taskRepository.removeRelationBetweenTaskAndEmployee(employeeId, taskId)
                    }
                    getAddedEmployeeListByTaskId(taskId)
                }
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
        }
    }

    fun saveTaskWithEmployees(taskId: Int, employee: Employee){
        try{
            viewModelScope.launch {
                Database.instance.withTransaction {
                    Log.d("TaskInfoViewModel", "reference")
                    if(taskRepository.getTaskWithEmployees(taskId).contains(employee)) return@withTransaction
                        val reference = EmployeesCrossReferences(taskId = taskId, employeeId = employee.id)
                        taskRepository.saveTaskWithEmployees(reference)
                        getAddedEmployeeListByTaskId(taskId)
                }
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
        }
    }
}