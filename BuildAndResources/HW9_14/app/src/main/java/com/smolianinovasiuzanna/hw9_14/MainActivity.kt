package com.smolianinovasiuzanna.hw9_14

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = """
            Build type = ${BuildConfig.BUILD_TYPE}
            Flavor = ${BuildConfig.FLAVOR}
            VersionCode = ${BuildConfig.VERSION_CODE}
            VersionName = ${BuildConfig.VERSION_NAME}   
            Application ID = ${BuildConfig.APPLICATION_ID}
        """
    }
}
