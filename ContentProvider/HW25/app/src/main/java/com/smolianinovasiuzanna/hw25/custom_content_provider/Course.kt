package com.smolianinovasiuzanna.hw25.custom_content_provider

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Course(
    val id: Long,
    val title: String
)
