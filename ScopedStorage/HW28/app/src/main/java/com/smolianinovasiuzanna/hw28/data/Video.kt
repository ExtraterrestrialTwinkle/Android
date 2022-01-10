package com.smolianinovasiuzanna.hw28.data

import android.net.Uri

data class Video(
    val id: Long,
    val title: String,
    val size: Int,
    val uri: Uri
){
    var favorite: Boolean = false
}
