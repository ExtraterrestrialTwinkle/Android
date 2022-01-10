package com.siuzannasmolianinova.hw35.data.networking

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET
    suspend fun downloadFile(
        @Url url: String
    ): ResponseBody
}