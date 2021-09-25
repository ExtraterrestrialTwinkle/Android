package com.smolianinovasiuzanna.hw13_11.com.smolianinovasiuzanna.hw13_11

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smolianinovasiuzanna.hw13_11.R
import com.smolianinovasiuzanna.hw13_11.databinding.ActivityDeeplinkBinding


class DeeplinkActivity:AppCompatActivity(R.layout.activity_deeplink) {

    private lateinit var binding: ActivityDeeplinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeeplinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntentData(intent)
        Log.d("Deeplink", "OnCreate")

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntentData(intent)
        Log.d("Deeplink", "onNewIntent")
    }

    private fun handleIntentData (intent: Intent?){
        intent?.data?.lastPathSegment.let { info -> binding.infoTextView.text = info }
        intent?.data?.host.let { name -> binding.studentNameTextView.text = name }
}
}