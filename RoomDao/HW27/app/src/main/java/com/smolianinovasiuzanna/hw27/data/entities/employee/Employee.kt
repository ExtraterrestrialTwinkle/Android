package com.smolianinovasiuzanna.hw27.data.entities.employee

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractContract
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.department.DepartmentContract
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract
import org.threeten.bp.Instant

@Entity(tableName = EmployeesContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Department::class,
            parentColumns = [DepartmentContract.Columns.ID],
            childColumns = [EmployeesContract.Columns.DEPARTMENT_ID]
        )
    ],
    indices = [
        Index(EmployeesContract.Columns.LAST_NAME)
    ]
)
@TypeConverters(EmployeeStatusConverter::class)
data class Employee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EmployeesContract.Columns.ID) val id: Int,
    @ColumnInfo(name = EmployeesContract.Columns.FIRST_NAME) val firstName: String,
    @ColumnInfo(name = EmployeesContract.Columns.LAST_NAME) val lastName: String,
    @ColumnInfo(name = EmployeesContract.Columns.AGE) val age: Int,
    @ColumnInfo(name = EmployeesContract.Columns.PHONE_NUMBER) val phoneNumber: String,
    @ColumnInfo(name = EmployeesContract.Columns.HOUR_SALARY) val hourSalary: Float,
    @ColumnInfo(name = EmployeesContract.Columns.STATUS) val status: EmployeeStatus = EmployeeStatus.WORKING,
    @ColumnInfo(name = EmployeesContract.Columns.STATUS_DURATION) val statusDuration: Int = 0, //Количество часов
    @ColumnInfo(name = EmployeesContract.Columns.DEPARTMENT_ID) val departmentId: Int,
    @ColumnInfo(name = EmployeesContract.Columns.EXPERIENCE) val experience: Int = 0
    ){

    override fun toString(): String {
        return this.lastName + this.firstName
    }

}





