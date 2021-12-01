package com.smolianinovasiuzanna.hw27.data.entities.factory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FactoryContract.TABLE_NAME)
data class Factory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FactoryContract.Columns.ID) val id: Int,
    @ColumnInfo(name = FactoryContract.Columns.TITLE) val title: String
)
