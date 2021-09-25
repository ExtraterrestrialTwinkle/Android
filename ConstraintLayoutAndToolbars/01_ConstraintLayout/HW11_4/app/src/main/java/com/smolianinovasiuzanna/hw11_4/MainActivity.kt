package com.smolianinovasiuzanna.hw11_4

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

import com.smolianinovasiuzanna.hw11_4.databinding.ActivityMainBinding

@GlideModule
class AppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val loadingProgressBar = ProgressBar(this)

        loadingProgressBar.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loadingProgressBar.apply {

            binding.loginButton.setOnClickListener {
                binding.editEmail.setText("")
                binding.editPassword.setText("")
                binding.frame.addView(loadingProgressBar)
                loadingProgressBar.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.frame.removeView(loadingProgressBar)
                        Toast.makeText(this@MainActivity, R.string.login_complete, Toast.LENGTH_SHORT).show()
                    },
                    2000
                )
            }
        }

        val imageView = binding.imageView
        Glide
            .with(this)
            .load("https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/c068f451582427.58f316acc1842.jpg")
            .into(imageView)
    }

    private fun validateFormData(email: String, password: String, isTermsChecked: Boolean) {
        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty() && isTermsChecked
    }
}
