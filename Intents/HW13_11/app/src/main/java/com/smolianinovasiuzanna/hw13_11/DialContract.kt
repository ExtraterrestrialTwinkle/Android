package com.smolianinovasiuzanna.hw13_11.com.smolianinovasiuzanna.hw13_11

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract

/*class DialContract : ActivityResultContract<String, Int?>() {


    override fun parseResult(resultCode: Int, intent: Intent?): Int? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent?.getIntExtra("my_result_key", 1)
    }

    override fun createIntent(context: Context, input: String?): Intent {
            return Intent(Intent.ACTION_CALL)
                .putExtra("my_input_key", input)
    }

}*/