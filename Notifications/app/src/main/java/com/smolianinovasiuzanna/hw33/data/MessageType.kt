package com.smolianinovasiuzanna.hw33.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.smolianinovasiuzanna.hw33.features.chat.db.ChatMessageContract
import com.smolianinovasiuzanna.hw33.features.chat.data.User
import com.smolianinovasiuzanna.hw33.features.chat.db.UserContract
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class MessageType() {

    @Entity(
        tableName = ChatMessageContract.TABLE_NAME,
        foreignKeys = [
            ForeignKey(entity = User::class,
                parentColumns = [UserContract.Columns.USER_ID],
                childColumns = [ChatMessageContract.Columns.ID]
            )
        ]
    )
    @JsonClass(generateAdapter = true)
    data class ChatMessage(
        @Json(name = "user_id")
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ChatMessageContract.Columns.ID)
        val userId: Long,
        @Json(name = "user_name")
        @ColumnInfo(name = ChatMessageContract.Columns.USER_NAME)
        val userName: String,
        @Json(name = "created_at")
        @ColumnInfo(name = ChatMessageContract.Columns.CREATED_AT)
        val createdAt: String,
        @Json(name = "text")
        @ColumnInfo(name = ChatMessageContract.Columns.MESSAGE_TEXT)
        val text: String,
        @ColumnInfo(name = ChatMessageContract.Columns.ADDRESSEE)
        val addressee: Long = -1
    ){

        override fun toString(): String {
            return "$userName: Message = $text"
        }

        companion object{
            const val TYPE = "chat_message"
        }
    }

    @JsonClass(generateAdapter = true)
    data class EventMessage(
        @Json(name = "title" ) val title: String,
        @Json(name = "description") val description: String,
        @Json(name = "image") val imageUrl: String?
    ){
        override fun toString(): String {
            return "$title $description"
        }

        companion object{
            const val TYPE = "event_message"
        }
    }

}
