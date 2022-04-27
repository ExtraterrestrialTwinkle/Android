package com.smolianinovasiuzanna.hw33.features.chat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.smolianinovasiuzanna.hw33.R
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_LONG
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_STRING
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.withArguments
import timber.log.Timber

class ChatActivity : AppCompatActivity() {

    private var extraLong: Long? = null
    private var extraString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        onNewIntent(intent)
        Timber.d("ChatActivity")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntentData(intent)
        Timber.d("onNewIntent = ${intent?.extras}")
        openFragment(extraLong, extraString)
    }

    private fun handleIntentData(intent: Intent?) {
        Timber.d("handleIntentData")
        extraLong = intent?.getLongExtra(BUNDLE_KEY_LONG, -1)
        extraString = intent?.getStringExtra(BUNDLE_KEY_STRING)
    }

    private fun openFragment(extraLong: Long?, extraString: String?){
        Timber.d("openFragment")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChatFragment.newInstance(extraLong ?:-1, extraString?: "John Doe"))
            .commit()
    }
}

