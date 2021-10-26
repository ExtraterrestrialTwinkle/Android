package com.smolianinovasiuzanna.hw24.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object Network {
    val client = OkHttpClient.Builder()
//        .addNetworkInterceptor(
//            HttpLoggingInterceptor()
//                .setLevel(HttpLoggingInterceptor.Level.BODY)
//        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://google.com")
        .client(client)
        .build()

    val api: Api
        get() = retrofit.create()
}