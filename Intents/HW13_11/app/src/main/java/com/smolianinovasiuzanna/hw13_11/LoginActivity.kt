package com.smolianinovasiuzanna.hw13_11.com.smolianinovasiuzanna.hw13_11

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.smolianinovasiuzanna.hw13_11.BuildConfig
import com.smolianinovasiuzanna.hw13_11.R
import com.smolianinovasiuzanna.hw13_11.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private lateinit var binding: ActivityLoginBinding

    private val launcherCall = prepareCall(ActivityResultContracts.Dial()) {isTelephone ->
       binding.contactTextView.text = isTelephone.toString()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(tag, "onCreate")

                binding.dialButton.setOnClickListener {
            val isPhoneNumberValid = Patterns.PHONE
                .matcher(binding.dialEditText.text.toString()).matches()

            if (!isPhoneNumberValid) {
                toast(R.string.incorrect_number)
            } else {
                Log.d(tag, "dialButton listener is on")
                takeResult()

            }
        }
    }

       private fun takeResult() {


        val dialIntent = Intent(Intent.ACTION_CALL)

          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
               dialIntent.setPackage("com.android.phone")
           }else{
               dialIntent.setPackage("com.android.server.telecom")
           }
           dialIntent.data = Uri.parse("tel:" + binding.dialEditText.text.toString())

        //if (dialIntent.resolveActivity(packageManager) != null) {
            launcherCall(binding.dialEditText.text.toString())
            Log.d(tag, "dialIntent is on" )
       // } else {
       //     toast(R.string.no_component)
       //     Log.d(tag, "dialIntent is off")
       // }
        }

   // private val getContact = registerForActivityResult(DialContract()) { str: Int? ->
    //    binding.contactTextView.text = str.toString()
   // }


    private fun toast(text: Int) {
        Toast.makeText(
            this@LoginActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }


    private val tag = "LoginActivity"
    object DebugLogger {
        fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG)
                Log.v(tag, message)
            Log.d(tag, message)
            Log.i(tag, message)
            Log.e(tag, message)
        }
    }

}
