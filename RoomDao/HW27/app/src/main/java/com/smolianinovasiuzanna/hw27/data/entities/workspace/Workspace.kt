package com.smolianinovasiuzanna.hw27.data.entities.workspace

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.smolianinovasiuzanna.hw27.data.entities.employee.Employee
import com.smolianinovasiuzanna.hw27.data.entities.employee.EmployeesContract

@Entity(tableName = WorkspaceContract.TABLE_NAME,
foreignKeys = [ForeignKey(
    entity = Employee::class,
    parentColumns = [EmployeesContract.Columns.ID],
    childColumns = [WorkspaceContract.Columns.ID]
    )]
)
data class Workspace(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WorkspaceContract.Columns.ID) val id: Int,
    @ColumnInfo(name = WorkspaceContract.Columns.EQUIPMENT) val equipment: Boolean,
    @ColumnInfo(name = WorkspaceContract.Columns.EMAIL) val email: String?,
    @ColumnInfo(name = WorkspaceContract.Columns.PASSWORD) val passwordHash: String?
)
