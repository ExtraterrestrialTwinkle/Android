package com.smolianinovasiuzanna.hw27.data.entities.project

import androidx.room.Entity

@Entity(primaryKeys = ["departmentId", "projectId"])
data class ProjectsCrossReferences(
    val departmentId: Int = 0,
    val projectId: Int = 0
)
