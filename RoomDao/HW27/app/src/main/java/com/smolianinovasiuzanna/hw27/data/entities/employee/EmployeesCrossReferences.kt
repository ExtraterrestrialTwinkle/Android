package com.smolianinovasiuzanna.hw27.data.entities.employee

import androidx.room.Entity

@Entity(primaryKeys = ["employeeId", "taskId"])
data class EmployeesCrossReferences(
    val employeeId: Int,
    val taskId: Int
)
