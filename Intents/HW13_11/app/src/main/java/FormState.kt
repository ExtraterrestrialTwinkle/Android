package com.smolianinovasiuzanna.hw13_11

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class FormState(val valid: Boolean, val message: String) : Parcelable