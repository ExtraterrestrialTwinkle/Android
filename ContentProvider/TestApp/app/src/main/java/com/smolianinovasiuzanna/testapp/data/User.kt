package com.smolianinovasiuzanna.testapp.data

import com.smolianinovasiuzanna.testapp.data.Course

data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val courses: List<Course>
)
