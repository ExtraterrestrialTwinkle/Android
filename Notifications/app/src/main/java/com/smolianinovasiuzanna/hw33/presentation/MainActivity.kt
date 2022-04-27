package com.smolianinovasiuzanna.hw33.presentation

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.smolianinovasiuzanna.hw33.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NotificationManagerCompat.from(this).cancelAll()//удаляем все оповещения при открытии приложения
    }
}