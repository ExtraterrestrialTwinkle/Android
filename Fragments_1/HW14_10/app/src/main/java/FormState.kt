package com.smolianinovasiuzanna.hw14_10

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class FormState(val valid: Boolean, val message: String) : Parcelable