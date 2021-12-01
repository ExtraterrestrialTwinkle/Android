package com.smolianinovasiuzanna.hw27.data.entities.task

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesCrossReferences

data class TasksWithEmployees(
@Embedded
val task: Task,
@Relation(
    parentColumn = TaskContract.Columns.ID,
    entityColumn = EmployeesContract.Columns.ID,
    associateBy = Junction(EmployeesCrossReferences::class)
)
val employees: List<Employee>
)

