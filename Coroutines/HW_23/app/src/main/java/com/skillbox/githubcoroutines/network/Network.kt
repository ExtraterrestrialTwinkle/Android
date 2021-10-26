package com.skillbox.githubcoroutines.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {

    private val headerInterceptor = Interceptor { chain ->
        chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "token ${Github.accessToken}"
            )
            .build()
            .let{chain.proceed(it)}
    }

    private val okhttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(headerInterceptor)
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient)
        .build()

    val githubApi: GithubApiInterface
        get() = retrofit.create()
}