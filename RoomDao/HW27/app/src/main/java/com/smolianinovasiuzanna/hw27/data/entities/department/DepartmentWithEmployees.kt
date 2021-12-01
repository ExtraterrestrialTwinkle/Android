package com.smolianinovasiuzanna.hw27.data.entities.department

import androidx.room.Embedded
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract


data class DepartmentWithEmployees(
    @Embedded
    val Department: Department,
    @Relation(
         parentColumn = DepartmentContract.Columns.ID,
         entityColumn = EmployeesContract.Columns.DEPARTMENT_ID
    )
    val employee: Employee?
)


