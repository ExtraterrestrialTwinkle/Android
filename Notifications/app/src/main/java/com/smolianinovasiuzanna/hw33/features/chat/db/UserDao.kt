package com.smolianinovasiuzanna.hw33.features.chat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smolianinovasiuzanna.hw33.features.chat.data.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun newUser(user: User)

    @Query ("SELECT * FROM ${UserContract.TABLE_NAME} WHERE ${UserContract.Columns.USER_ID} =:userId")
    suspend fun getUser(userId: Long): User
}