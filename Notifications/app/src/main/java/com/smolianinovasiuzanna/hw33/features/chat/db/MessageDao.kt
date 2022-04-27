package com.smolianinovasiuzanna.hw33.features.chat.db

import androidx.room.*
import com.smolianinovasiuzanna.hw33.data.MessageType

@Dao
interface MessageDao {

    @Query("SELECT * FROM ${ChatMessageContract.TABLE_NAME} WHERE ${ChatMessageContract.Columns.ID} = :userId UNION SELECT * FROM ${ChatMessageContract.TABLE_NAME} WHERE ${ChatMessageContract.Columns.ADDRESSEE} = :userId ORDER BY ${ChatMessageContract.Columns.CREATED_AT} ASC")
    suspend fun getMessagesByUserId(userId: Long): List<MessageType.ChatMessage>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun newMessage(message: MessageType.ChatMessage)
}