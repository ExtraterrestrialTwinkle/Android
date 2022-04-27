package com.smolianinovasiuzanna.hw33.core

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smolianinovasiuzanna.hw33.presentation.MainActivity
import com.smolianinovasiuzanna.hw33.R
import com.smolianinovasiuzanna.hw33.data.FirebaseToken.firebaseToken
import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.data.utils.convertMessage
import com.smolianinovasiuzanna.hw33.features.chat.db.utils.writeToDatabase
import com.smolianinovasiuzanna.hw33.features.chat.ui.ChatActivity
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_LONG
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_STRING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class MessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        firebaseToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("${message.data["data"]}, ${message.data["type"]}")

        when(message.data["type"]){
            MessageType.EventMessage.TYPE -> showNewEventNotification(message)
            MessageType.ChatMessage.TYPE -> showChatMessageNotification(message)
        }
    }

    private fun chatIntent(requestCode: Int, userId: Long, userName: String): PendingIntent{
        // интент, открываеющий  MainActivity при нажатии на оповещение
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(BUNDLE_KEY_LONG, userId)
        intent.putExtra(BUNDLE_KEY_STRING, userName)
        Timber.d("chatIntent")
        // обертка над интентом, которая позволяет передать его в другой процесс таким образом,
        // что у процесса появляется возможность запустить наш компонент так, будто бы он запускается в нашем приложении
        return PendingIntent.getActivity(this, requestCode, intent, FLAG_UPDATE_CURRENT)
    }

    private fun showChatMessageNotification(message: RemoteMessage){
        val chatMessage = convertMessage(MessageType.ChatMessage::class.java, message) ?: return

        CoroutineScope(Dispatchers.IO).launch{
            try{
                chatMessage.writeToDatabase()
                Timber.d("$chatMessage write to db")
            } catch(t: Throwable){
                Timber.e(t)
            }
        }

        val notification = NotificationCompat.Builder(
            this,
            NotificationChannels.PRIORITY_CHANNEL_ID
        )
            .setContentTitle("You have a new message from ${chatMessage.userName}")
            .setContentText(chatMessage.text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_message)
            .setAutoCancel(true)
            .setContentIntent(chatIntent(REQUEST_CODE_CHAT, chatMessage.userId, chatMessage.userName))
            .build()

        NotificationManagerCompat.from(this)
            .notify(chatMessage.userId.toInt(), notification) // Change Id!!!!!
    }

    private fun eventIntent(requestCode: Int): PendingIntent{
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, requestCode, intent, 0)
    }

    private fun showNewEventNotification(message: RemoteMessage){
        val eventMessage = convertMessage(MessageType.EventMessage::class.java, message) ?: return

        Timber.d(eventMessage.toString())

        val largeIconBitmap = LargeIcon(this).loadLargeIcon(eventMessage.imageUrl)

        val notification = NotificationCompat.Builder(
            this,
            NotificationChannels.NON_PRIORITY_CHANNEL_ID
        )
            .setContentTitle(eventMessage.title)
            .setContentText(eventMessage.description)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_event_note)
            .setLargeIcon(largeIconBitmap)
            .setContentIntent(eventIntent(REQUEST_CODE_EVENT))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this)
            .notify(NON_PRIORITY_MESSAGE_ID, notification)
    }

    companion object{
        private const val NON_PRIORITY_MESSAGE_ID = 1
        private const val REQUEST_CODE_CHAT = 12345
        private const val REQUEST_CODE_EVENT = 98765
        private const val KEY = "key"
    }
}
