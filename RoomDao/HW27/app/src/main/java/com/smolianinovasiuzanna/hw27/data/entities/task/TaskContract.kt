package com.smolianinovasiuzanna.hw27.data.entities.task


object TaskContract {
    const val TABLE_NAME = "tasks"

    object Columns{
        const val ID = "taskId"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val DURATION = "duration"
        const val STATUS = "status"
        const val PROJECT_ID = "project_id"
    }
}