package com.smolianinovasiuzanna.hw24

import android.app.Application
import android.os.StrictMode
import net.openid.appauth.BuildConfig

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}