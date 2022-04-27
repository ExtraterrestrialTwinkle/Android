package com.smolianinovasiuzanna.hw33.features.chat.data


import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.features.chat.db.Database

class ChatRepository {

    private val messageDao = Database.instance.messageDao()
    private val userDao = Database.instance.userDao()

    suspend fun receiveAllMessagesFromUser(userId: Long): List<MessageType.ChatMessage> {
        return messageDao.getMessagesByUserId(userId)
    }

    suspend fun sendMessage(message: MessageType.ChatMessage){
        // пишем исходящее сообщение только в БД (fake chat)
        val user = User(message.userId, message.userName)
        newUser(user)
        messageDao.newMessage(message)
    }

    suspend fun newUser(user: User) {
        if(userDao.getUser(user.userId) == user) return
        else {
            userDao.newUser(user)
        }
    }
}