package com.smolianinovasiuzanna.hw33.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {

    const val PRIORITY_CHANNEL_ID = "priority"
    const val NON_PRIORITY_CHANNEL_ID = "non-priority"
    const val PROGRESS_CHANNEL_ID = "progress"

    fun create (context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createPriorityChannel(context)
            createNonPriorityChannel(context)
            createProgressChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPriorityChannel(context: Context){
        val name = "Priority"
        val channelDescription = "Urgent messages"
        val priority = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(PRIORITY_CHANNEL_ID, name, priority).apply{
            description = channelDescription
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNonPriorityChannel(context: Context){
        val name = "Non-priority"
        val channelDescription = "Application news messages"
        val priority = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(NON_PRIORITY_CHANNEL_ID, name, priority).apply{
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProgressChannel(context: Context){
        val name = "Progress"
        val channelDescription = "Downloading file"
        val priority = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(PROGRESS_CHANNEL_ID, name, priority).apply{
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

}