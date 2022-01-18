package com.siuzannasmolianinova.hw36.data.networking

import com.siuzannasmolianinova.hw36.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {
    private val authInterceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .build()
        val newRequest = chain.request().newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(EnumConverterFactory())
        .build()

    val api: NewsApi
        get() = retrofit.create()
}