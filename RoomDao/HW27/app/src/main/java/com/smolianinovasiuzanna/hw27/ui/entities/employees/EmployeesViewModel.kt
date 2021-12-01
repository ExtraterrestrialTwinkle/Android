package com.smolianinovasiuzanna.hw27.ui.entities.employees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class EmployeesViewModel : ViewModel() {
    private val repository = EmployeeRepository()

    private val employeesLiveData = MutableLiveData<List<Employee>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val employees: LiveData<List<Employee>>
        get() = employeesLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun loadList(){
        viewModelScope.launch(){
            try {
                employeesLiveData.postValue(repository.getAllEmployees())
            } catch (t: Throwable){
                errorLiveData.postValue(t)
                employeesLiveData.postValue(emptyList())
            }
        }
    }

    fun deleteEmployee(employee: Employee){
        viewModelScope.launch {
            try{
                repository.deleteEmployee(employeeId = employee.id)
                loadList()
            } catch (t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }
}