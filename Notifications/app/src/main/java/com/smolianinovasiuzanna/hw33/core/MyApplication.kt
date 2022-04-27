package com.smolianinovasiuzanna.hw33.core

import android.app.Application
import com.smolianinovasiuzanna.hw33.core.NotificationChannels
import com.smolianinovasiuzanna.hw33.features.chat.db.Database
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NotificationChannels.create(this)
        Database.init(this)
    }
}