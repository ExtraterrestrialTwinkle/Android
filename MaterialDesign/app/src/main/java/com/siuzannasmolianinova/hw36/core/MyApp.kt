package com.siuzannasmolianinova.hw36.core

import android.app.Application
import com.siuzannasmolianinova.hw36.data.db.Database
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Database.init(this)
    }
}