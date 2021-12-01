package com.smolianinovasiuzanna.hw27.data.entities.contract

import androidx.room.Embedded
import androidx.room.Relation
import com.smolianinovasiuzanna.hw27.data.entities.department.Department
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectContract

data class ContractWithRelations(
    @Embedded val contract: Contract,
    @Relation(
        parentColumn = ContractContract.Columns.ID,
        entityColumn = ProjectContract.Columns.CONTRACT_ID
    )
    val project: Project
)
