package com.smolianinovasiuzanna.hw27.ui.adding.add_employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.repository.DepartmentRepository
import com.smolianinovasiuzanna.hw27.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class AddEmployeeViewModel : ViewModel() {
    private val repository = EmployeeRepository()
    private val departmentRepository = DepartmentRepository()

    private val saveSuccessLiveData = MutableLiveData<Unit>()
    private val departmentListLiveData = MutableLiveData<List<Department>>()
    private val employeeDepartmentTitleLiveData = MutableLiveData<String>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val existingEmployeeLiveData = MutableLiveData<Employee>()

    val existingEmployee: LiveData<Employee>
        get() = existingEmployeeLiveData

    val saveSuccess: LiveData<Unit>
        get() = saveSuccessLiveData

    val departmentList: LiveData<List<Department>>
        get() = departmentListLiveData

    val employeeDepartmentTitle: LiveData<String>
        get() = employeeDepartmentTitleLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun init(id: Int) {
        viewModelScope.launch {
            try {
                val employee = repository.getEmployeeById(id)
                if (employee != null) {
                    existingEmployeeLiveData.postValue(employee)
                    employeeDepartmentTitleLiveData
                        .postValue(departmentRepository.getDepartmentById(employee.departmentId).departmentName)
                }
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

    fun getDepartmentsList(){
        try {
            viewModelScope.launch{
                departmentListLiveData.postValue(departmentRepository.getAllDepartments())
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
            departmentListLiveData.postValue(emptyList())
        }
    }
    fun save(
        id: Int,
        firstName: String,
        lastName: String,
        age: Int,
        phoneNumber: String,
        hourSalary: Float,
        departmentId: Int
    ) {
        viewModelScope.launch {
            try {
                val employee = Employee(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    age = age,
                    phoneNumber = phoneNumber,
                    hourSalary = hourSalary,
                    departmentId = departmentId
                )
                if (id == 0) {
                    repository.saveEmployees(employee)
                } else {
                    repository.updateEmployee(employee)
                }
                saveSuccessLiveData.postValue(Unit)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

}