package com.smolianinovasiuzanna.hw15_10

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


@kotlinx.parcelize.Parcelize
data class FormState(
    val valid: Boolean,
    val position: Int,
    val checked: BooleanArray
    ): Parcelable


@kotlinx.parcelize.Parcelize
data class State(val articles: List<OnboardingScreen>): Parcelable
