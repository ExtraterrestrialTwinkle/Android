package com.skillbox.githubcoroutines.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerInfo(
    @Json(name = "login") val login: String? = "",
    @Json(name = "avatar_url") val avatar: String? = ""
)
