package com.smolianinovasiuzanna.hw27.data.entities.employee

import androidx.room.TypeConverter

enum class EmployeeStatus {
    WORKING,
    ONVACATIONS
}

class EmployeeStatusConverter{
    @TypeConverter
    fun convertStatusToString(status: EmployeeStatus): String = status.name

    @TypeConverter
    fun convertStringToStatus(statusString: String): EmployeeStatus = EmployeeStatus.valueOf(statusString)
}
