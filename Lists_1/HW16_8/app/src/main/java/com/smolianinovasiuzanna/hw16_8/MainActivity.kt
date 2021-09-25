package com.smolianinovasiuzanna.hw16_8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    var state:ActivityState = ActivityState(false, "false")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ListFragment())
            .commit()
            state = ActivityState(true, "true")

    } else {
            state = savedInstanceState.getParcelable(KEY) ?: error("Error")

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, state)
    }

    companion object{
        private const val KEY = "activity_key"
    }

}