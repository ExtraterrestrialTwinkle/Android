package com.smolianinovasiuzanna.hw27.data.entities.contract

import androidx.room.TypeConverter
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeeStatus

enum class ContractStatus{
    PLANNED,
    INPROGRESS,
    ENDED
}

class ContractStatusConverter{
    @TypeConverter
    fun convertStatusToString(status: ContractStatus): String = status.name

    @TypeConverter
    fun convertStringToStatus(statusString: String): ContractStatus = ContractStatus.valueOf(statusString)
}