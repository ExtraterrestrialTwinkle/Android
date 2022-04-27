package com.smolianinovasiuzanna.hw34.data.networking

import com.smolianinovasiuzanna.hw34.data.movie_database.EnumConverterFactory
import com.smolianinovasiuzanna.hw34.data.networking.ApiKey.API_KEY
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber
import java.util.concurrent.TimeUnit

object Network {

    private val authInterceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .build()
        val newRequest = chain.request().newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor ( authInterceptor )
        .addNetworkInterceptor (
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(EnumConverterFactory())
        .client(client)
        .build()

    val api: Api
        get() = retrofit.create()
}