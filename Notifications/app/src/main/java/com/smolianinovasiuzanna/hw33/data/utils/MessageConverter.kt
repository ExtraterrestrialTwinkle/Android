package com.smolianinovasiuzanna.hw33.data.utils

import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.Moshi

fun <T>convertMessage(clazz: Class<T>, message: RemoteMessage): T? {
    val moshi = Moshi.Builder().build()
    val moshiAdapter = moshi.adapter(clazz).nonNull()
    return try{
        moshiAdapter.fromJson(message.data["data"])
    } catch(e: Exception){
        null
    }
}
