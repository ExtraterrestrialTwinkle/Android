package com.smolianinovasiuzanna.hw27.data.entities.project

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractContract
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.department.DepartmentContract
import org.threeten.bp.Instant

@Entity(tableName = ProjectContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Contract::class,
            parentColumns = [ContractContract.Columns.ID],
            childColumns = [ProjectContract.Columns.CONTRACT_ID]
        )
    ]
)
@TypeConverters(ProjectStatusConverter::class)
data class Project(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ProjectContract.Columns.ID) val id: Int,
    @ColumnInfo(name = ProjectContract.Columns.TITLE) val title: String,
    @ColumnInfo(name = ProjectContract.Columns.DURATION) val duration: Int, //количество дней
    @ColumnInfo(name = ProjectContract.Columns.STATUS) val status: ProjectStatus,
    @ColumnInfo(name = ProjectContract.Columns.CONTRACT_ID) val contractId: Int,
)

