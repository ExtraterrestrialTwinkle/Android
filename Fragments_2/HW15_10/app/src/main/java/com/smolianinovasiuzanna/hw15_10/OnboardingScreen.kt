package com.smolianinovasiuzanna.hw15_10

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
@kotlinx.parcelize.Parcelize
data class OnboardingScreen(
    val id: Int,
    @DrawableRes val imageRes: Int,
    @StringRes val nameRes: Int,
    @StringRes val infoRes: Int,
    @DrawableRes val iconRes:Int,
    val tags: List<ArticleType>
): Parcelable
