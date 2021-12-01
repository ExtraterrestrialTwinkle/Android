package com.smolianinovasiuzanna.hw27.data.entities.department

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract

@Entity(tableName = DepartmentContract.TABLE_NAME,
//foreignKeys = [ForeignKey(
//    entity = Project::class,
//    parentColumns = [ProjectContract.Columns.ID],
//    childColumns = [DepartmentContract.Columns.PROJECT_ID]
//)]
    )
data class Department(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DepartmentContract.Columns.ID) val id: Int,
    @ColumnInfo(name = DepartmentContract.Columns.NAME) val departmentName: String
){
    override fun toString(): String {
        return this.departmentName
    }
}
