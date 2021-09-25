package com.smolianinovasiuzanna.hw12_12

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class FormState(val valid: Boolean, val message: String) : Parcelable