package com.smolianinovasiuzanna.hw33.features.chat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.features.chat.data.User

@Database(entities = [
    User::class,
    MessageType.ChatMessage::class
], version = ChatDatabase.DB_VERSION
)
abstract class ChatDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao

    companion object{
        const val DB_VERSION = 1
        const val DB_NAME = "chat_database"
    }
}