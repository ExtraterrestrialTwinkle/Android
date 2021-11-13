package com.smolianinovasiuzanna.hw24.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

object Network {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
        .readTimeout(30, TimeUnit.SECONDS)   // socket timeout
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