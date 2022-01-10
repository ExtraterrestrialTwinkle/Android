package com.smolianinovasiuzanna.hw34.core


import android.app.Application
import com.smolianinovasiuzanna.hw34.data.movie_database.Database
import timber.log.Timber

class FlowApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(this)
        Timber.plant(Timber.DebugTree())
    }
}