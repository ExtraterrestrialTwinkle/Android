package com.smolianinovasiuzanna.hw33.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET
    suspend fun downloadFile(
        @Url url: String
    ): ResponseBody
}