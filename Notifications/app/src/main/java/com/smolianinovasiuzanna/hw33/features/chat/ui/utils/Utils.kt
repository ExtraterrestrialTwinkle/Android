package com.smolianinovasiuzanna.hw33.features.chat.ui.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

fun <T: Fragment> T.withArguments(action: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(action)
    }
}