package com.smolianinovasiuzanna.hw33.features.chat.db.utils

import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.features.chat.data.User
import com.smolianinovasiuzanna.hw33.features.chat.db.Database

suspend fun MessageType.ChatMessage.writeToDatabase(){
    val messageDao = Database.instance.messageDao()
    val userDao = Database.instance.userDao()
    val user = User(this.userId, this.userName)
    if(userDao.getUser(this.userId) == user){
        messageDao.newMessage(this)
    } else {
        userDao.newUser(user)
        messageDao.newMessage(this)
    }
}