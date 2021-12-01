package com.smolianinovasiuzanna.hw27.data.entities.task

import androidx.room.TypeConverter
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeeStatus

enum class TaskStatus{
    PLANNED,
    INPROGRESS,
    ENDED
}

class TaskStatusConverter{
    @TypeConverter
    fun convertStatusToString(status: TaskStatus): String = status.name

    @TypeConverter
    fun convertStringToStatus(statusString: String): TaskStatus = TaskStatus.valueOf(statusString)
}