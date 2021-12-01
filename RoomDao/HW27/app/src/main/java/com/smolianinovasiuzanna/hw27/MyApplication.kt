package com.smolianinovasiuzanna.hw27

import android.app.Application
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.repository.FactoryRepository

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }
}