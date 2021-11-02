package com.smolianinovasiuzanna.hw25

import android.app.Application
import android.os.StrictMode

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
//        if(BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()
//                    .penaltyDeath()
//                    .build()
//            )
//        }
    }
}