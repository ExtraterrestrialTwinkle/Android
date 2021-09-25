     package com.smolianinovasiuzanna.hw17

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.smolianinovasiuzanna.hw17.actors.ActorsFragment

     class MainActivity : FragmentActivity(), OnBackPressedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

         override fun onBackPressed() {
             var backPressedListener: OnBackPressedListener? = null
             for (fragment in supportFragmentManager.fragments) {
                 if (fragment is OnBackPressedListener) {
                     backPressedListener = fragment
                     break
                 }
             }
             if (backPressedListener != null) {
                 backPressedListener.onBackPressed()
             } else {
                 super.onBackPressed()
             }
         }

     }