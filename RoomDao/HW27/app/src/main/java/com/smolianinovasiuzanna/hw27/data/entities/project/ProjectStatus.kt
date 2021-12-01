package com.smolianinovasiuzanna.hw27.data.entities.project

import androidx.room.TypeConverter
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeeStatus

enum class ProjectStatus{
    PLANNED,
    INPROGRESS,
    ENDED
}

class ProjectStatusConverter{
    @TypeConverter
    fun convertStatusToString(status: ProjectStatus): String = status.name

    @TypeConverter
    fun convertStringToStatus(statusString: String): ProjectStatus = ProjectStatus.valueOf(statusString)
}