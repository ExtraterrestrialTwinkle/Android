package com.smolianinovasiuzanna.hw27.data.entities.project

import androidx.room.Embedded
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractContract
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskContract

data class ProjectWithTasks(
    @Embedded
    val project: Project,
    @Relation(
        parentColumn = ProjectContract.Columns.ID,
        entityColumn = TaskContract.Columns.PROJECT_ID
    )
    val task: List<Task>
)
