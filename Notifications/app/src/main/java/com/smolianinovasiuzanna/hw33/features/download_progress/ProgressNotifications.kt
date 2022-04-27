package com.smolianinovasiuzanna.hw33.features.download_progress

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.smolianinovasiuzanna.hw33.R
import com.smolianinovasiuzanna.hw33.core.NotificationChannels
import kotlinx.coroutines.delay

class ProgressNotifications(private val context: Context) {

    fun showInfiniteProgressNotification(){
        val infiniteProgressNotification = NotificationCompat.Builder(
            context,
            NotificationChannels.PROGRESS_CHANNEL_ID
        )
            .setContentTitle("Downloading")
            .setContentText("Prepare downloading")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(0, 0, true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(PROGRESS_NOTIFICATION_ID, infiniteProgressNotification)
    }

    fun showDownloadProgressNotification(progress: Int, maxProgress: Int){
            val downloadProgressNotification = NotificationCompat.Builder(
                context,
                NotificationChannels.PROGRESS_CHANNEL_ID
            )
                .setContentTitle("Downloading")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_download)
                .setProgress(maxProgress, progress,false)
                .build()

            NotificationManagerCompat.from(context)
                .notify(PROGRESS_NOTIFICATION_ID, downloadProgressNotification)
    }

    fun showFinalNotification(){
        val finalNotification = NotificationCompat.Builder(
            context,
            NotificationChannels.PROGRESS_CHANNEL_ID
        )
            .setContentText("Download completed")
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(0,0, false)
            .build()

        NotificationManagerCompat.from(context)
            .notify(PROGRESS_NOTIFICATION_ID, finalNotification)
    }

    fun showErrorNotification(){
        val errorNotification = NotificationCompat.Builder(
            context,
            NotificationChannels.PROGRESS_CHANNEL_ID
        )
            .setContentText("Download error!")
            .setSmallIcon(R.drawable.ic_error)
            .build()

        NotificationManagerCompat.from(context)
            .notify(PROGRESS_NOTIFICATION_ID, errorNotification)
    }

    companion object{
        private const val PROGRESS_NOTIFICATION_ID = 984663
    }

}