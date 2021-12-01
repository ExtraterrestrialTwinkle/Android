package com.smolianinovasiuzanna.hw27.ui.entities.departments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.repository.DepartmentRepository
import kotlinx.coroutines.launch

class DepartmentsViewModel : ViewModel() {
    private val repository = DepartmentRepository()
    private val departmentsLiveData = MutableLiveData<List<Department>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val departments: LiveData<List<Department>>
        get() = departmentsLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun loadList(){
        viewModelScope.launch(){
            try {
                departmentsLiveData.postValue(repository.getAllDepartments())
            } catch (t: Throwable){
                errorLiveData.postValue(t)
                departmentsLiveData.postValue(emptyList())
            }
        }
    }

    fun deleteDepartment(department: Department) {
        viewModelScope.launch {
            try{
                repository.deleteDepartment(departmentId = department.id)
                loadList()
            } catch (t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }

}