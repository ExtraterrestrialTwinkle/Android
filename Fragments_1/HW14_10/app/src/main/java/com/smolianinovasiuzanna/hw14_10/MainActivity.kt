package com.smolianinovasiuzanna.hw14_10

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.smolianinovasiuzanna.hw14_10.com.smolianinovasiuzanna.hw14_10.*
import com.smolianinovasiuzanna.hw14_10.databinding.ActivityMainBinding


@GlideModule
class AppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity(), ItemSelectListener, OnBackPressedListener {

    private lateinit var binding: ActivityMainBinding
    private var valid: FormState = FormState(false, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valid = FormState(true, "")

        if (savedInstanceState == null) {
            showLoginFragment()

        }
        Log.d("MainActivity", "onCreate")

        if (savedInstanceState != null) {
            valid = savedInstanceState.getParcelable(KEY) ?: error("Error")

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, valid)
        Log.d("MainActivity", "onSaveInstanceState")
    }


    private fun showLoginFragment(){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
    }

    override fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.fragmentContainer, MainFragment())
            .addToBackStack("Main")
            .commit()
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
         companion object {
        private const val KEY = "key"
    }
}
