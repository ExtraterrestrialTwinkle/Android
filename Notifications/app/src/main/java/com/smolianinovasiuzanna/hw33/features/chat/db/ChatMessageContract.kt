package com.smolianinovasiuzanna.hw33.features.chat.db

object ChatMessageContract {

    const val TABLE_NAME = "message"

    object Columns {
        const val ID = "id"
        const val USER_NAME = "user_name"
        const val MESSAGE_TEXT = "message_text"
        const val CREATED_AT = "created_at"
        const val ADDRESSEE = "addressee"
    }
}