package com.smolianinovasiuzanna.hw33.features.chat.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smolianinovasiuzanna.hw33.features.chat.db.UserContract

@Entity(tableName = UserContract.TABLE_NAME)
data class User(
    @PrimaryKey
    @ColumnInfo(name = UserContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = UserContract.Columns.USER_NAME)
    val userName: String

)
