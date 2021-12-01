package com.smolianinovasiuzanna.hw27.ui.adding.add_department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.repository.DepartmentRepository
import kotlinx.coroutines.launch

class AddDepartmentViewModel : ViewModel() {
    private val repository = DepartmentRepository()
    private val errorLiveData = MutableLiveData<Throwable>()

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun save(
        name: String
    ) {
        val department = Department(
            id = 0,
            departmentName = name

        )
        viewModelScope.launch {
            try {
                repository.saveDepartments(department)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}