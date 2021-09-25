package com.smolianinovasiuzanna.hw12_12

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Contacts.SettingsColumns.KEY
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.smolianinovasiuzanna.hw12_12.databinding.ActivityMainBinding

@GlideModule
class AppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity() {

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
        binding.ANRButton.setOnClickListener {
            Thread.sleep(10000)
        }

        binding.loginButton.setOnClickListener {
            if (binding.editEmail.text.toString().contains('@')
            ) {
                binding.error.removeView(errorText)
                valid = FormState(true, "")
                binding.frame.addView(loadingProgressBar)
                loadingProgressBar.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.frame.removeView(loadingProgressBar)
                        Toast.makeText(
                            this@MainActivity,
                            R.string.login_complete,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    2000
                )
            } else {
                valid = FormState(false, "E-mail is incorrect")
                binding.error.addView(errorText)
                errorText.visibility = View.VISIBLE
                errorText.text = valid.message
            }
            binding.editEmail.setText("")
            binding.editPassword.setText("")
        }

        val imageView = binding.imageView
        Glide
            .with(this)
            .load("https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/c068f451582427.58f316acc1842.jpg")
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
