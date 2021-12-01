package com.smolianinovasiuzanna.hw27.data.entities.employee

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectsCrossReferences
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract

data class EmployeesWithTasks(
    @Embedded
    val employee: Employee,
    @Relation(
        parentColumn = EmployeesContract.Columns.ID,
        entityColumn = TaskContract.Columns.ID,
        associateBy = Junction(EmployeesCrossReferences::class)
    )
    val tasks: List<Task?>
)