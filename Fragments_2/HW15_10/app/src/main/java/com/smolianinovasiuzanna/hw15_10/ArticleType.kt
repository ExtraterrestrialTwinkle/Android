package com.smolianinovasiuzanna.hw15_10

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
enum class ArticleType (val tag: String) : Parcelable {
    VOLUMETRIC_GLASSWARE("Мерная посуда"),
    LABORATORY_GLASSWARE("Посуда общего назначения"),
    SPECIAL_GLASSWARE("Посуда специального назначения")
}