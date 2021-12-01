package com.smolianinovasiuzanna.hw27.data.entities.project

object ProjectContract {
    const val TABLE_NAME = "projects"

    object Columns{
        const val ID = "projectId"
        const val TITLE = "title"
        const val DURATION = "duration"
        const val STATUS = "status"
        const val CONTRACT_ID = "contract_id"
    }
}