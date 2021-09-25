package com.smolianinovasiuzanna.hw13_11

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.smolianinovasiuzanna.hw13_11.com.smolianinovasiuzanna.hw13_11.LoginActivity
import com.smolianinovasiuzanna.hw13_11.databinding.ActivityMainBinding

@GlideModule
class AppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private var valid: FormState = FormState(false, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingProgressBar = ProgressBar(this)
        loadingProgressBar.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val errorText = TextView(this)
        errorText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        if (savedInstanceState != null) {
            valid = savedInstanceState.getParcelable<FormState>(KEY) ?: error("Error")
            binding.error.addView(errorText)
            errorText.visibility = View.VISIBLE
            errorText.text = valid.message
        }

        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                validateFormData(
                    email = p0.toString(),
                    password = binding.editPassword.text.toString(),
                    isTermsChecked = binding.checkbox.isChecked
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateFormData(
                    email = p0.toString(),
                    password = binding.editPassword.text.toString(),
                    isTermsChecked = binding.checkbox.isChecked
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            validateFormData(
                email = binding.editEmail.text.toString(),
                password = binding.editPassword.text.toString(),
                isTermsChecked = isChecked
            )
        }
        binding.DeeplinkButton.setOnClickListener {
            val goUrl = Intent(Intent.ACTION_VIEW)
            goUrl.data = Uri.parse("http://smolianinovasiuzanna.com/info/123456789/")
                startActivity(goUrl)
                finish()
        }

        binding.loginButton.setOnClickListener {

            val isEmailValid = Patterns.EMAIL_ADDRESS
                .matcher(binding.editEmail.text.toString()).matches()

            if (binding.editPassword.text.toString().contains(' ')) {
                valid = FormState(false, "Password is incorrect")
                binding.error.addView(errorText)
                errorText.visibility = View.VISIBLE
                errorText.text = valid.message
            } else if (!isEmailValid) {
                toast(R.string.incorrect_email)
            } else {
                binding.error.removeView(errorText)
                valid = FormState(true, "")
                binding.frame.addView(loadingProgressBar)
                loadingProgressBar.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.frame.removeView(loadingProgressBar)
                    },
                    2000
                )
                val activityClass: Class<LoginActivity> = LoginActivity::class.java
                val loginActivityIntent = Intent(this, activityClass)
                startActivity(loginActivityIntent)
                finish()
            }
            binding.editEmail.setText("")
            binding.editPassword.setText("")
        }

        val imageView = binding.imageView
        Glide
            .with(this)
            .load("https://img.fonwall.ru/o/os/trava-cvety-makro.jpg")
            .into(imageView)

        DebugLogger.d(tag, "OnCreate ${hashCode()}")
    }

    override fun onStart() {
        super.onStart()
        DebugLogger.d(tag, "OnStart ")
    }

    override fun onResume() {
        super.onResume()
        DebugLogger.d(tag, "OnResume")
    }

    override fun onPause() {
        super.onPause()
        DebugLogger.d(tag, "OnPause")
    }

    override fun onStop() {
        super.onStop()
        DebugLogger.d(tag, "OnStop")
    }

    override fun onRestart() {
        super.onRestart()
        DebugLogger.d(tag, "OnRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLogger.d(tag, "OnDestroy")
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        DebugLogger.d(tag, "OnTopResumedActivityChanged ${hashCode()} $isTopResumedActivity")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, valid)
    }

    private fun validateFormData(email: String, password: String, isTermsChecked: Boolean) {
        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty() && isTermsChecked
    }

    private fun toast(text: Int) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }


    private val tag = "MainActivity"
    object DebugLogger {
        fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG)
                Log.v(tag, message)
            Log.d(tag, message)
            Log.i(tag, message)
            Log.e(tag, message)
        }
    }

    companion object {
        private const val KEY = "key"
    }
}
