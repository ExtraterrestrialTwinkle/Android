package com.smolianinovasiuzanna.hw27.data.entities.task

import androidx.room.*
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract
import org.threeten.bp.Instant

@Entity(tableName = TaskContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = [ProjectContract.Columns.ID],
            childColumns = [TaskContract.Columns.PROJECT_ID]
        )
    ]
)
@TypeConverters(TaskStatusConverter::class)
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TaskContract.Columns.ID) val id: Int,
    @ColumnInfo(name = TaskContract.Columns.TITLE) val title: String,
    @ColumnInfo(name = TaskContract.Columns.DESCRIPTION) val description: String,
    @ColumnInfo(name = TaskContract.Columns.DURATION) val duration: Int, // Количество дней
    @ColumnInfo(name = TaskContract.Columns.STATUS) val status: TaskStatus,
    @ColumnInfo(name = TaskContract.Columns.PROJECT_ID) val projectId: Int
)

