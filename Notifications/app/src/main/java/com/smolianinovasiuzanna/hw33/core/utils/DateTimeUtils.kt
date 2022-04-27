package com.smolianinovasiuzanna.hw33.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun fromMillisToTimeString(millis: Long) : String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(millis)
    }
}
