package com.smolianinovasiuzanna.hw27.data.entities.contractor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ContractorContract.TABLE_NAME)
data class Contractor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ContractorContract.Columns.ID) val id: Int,
    @ColumnInfo(name = ContractorContract.Columns.NAME) val name: String
)
