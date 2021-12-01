package com.smolianinovasiuzanna.hw27.data.entities.project

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.department.DepartmentContract

data class ProjectWithDepartments(
    @Embedded val project: Project,
    @Relation(
        parentColumn = ProjectContract.Columns.ID,
        entityColumn = DepartmentContract.Columns.ID,
        associateBy = Junction(ProjectsCrossReferences::class)
    )
    val departments: List<Department>
)
