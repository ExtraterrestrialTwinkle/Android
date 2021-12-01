package com.smolianinovasiuzanna.hw27.data.entities.employee

object EmployeesContract {
    const val TABLE_NAME = "employees"
    object Columns{
        const val ID = "employeeId"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val AGE = "age"
        const val PHONE_NUMBER = "phone_number"
        const val HOUR_SALARY = "hour_salary"
        const val STATUS = "status"
        const val STATUS_DURATION = "status_duration"
        const val DEPARTMENT_ID = "departmentId"
        const val EXPERIENCE = "experience"
    }

}