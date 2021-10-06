package com.smolianinovasiuzanna.hw22_1

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object Network {
// Создаю интерцептор для добавления APIKEY
    private val authInterceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val newUrl = request.url.newBuilder() //модифицированный url
            .addQueryParameter("apikey", API_KEY)
            .build()
        val newRequest = chain.request().newBuilder() // запрос с новым url'ом
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    val client = OkHttpClient.Builder()
        .addNetworkInterceptor (authInterceptor)
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()


    fun searchMovieCall(
        title: String,
    ): Call {
Log.d ("searchMovieCall", "call")
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("www.omdbapi.com")
            //.addQueryParameter("apikey", API_KEY)
            .addQueryParameter("t", title)
            .build()

        Log.d("Url = ", "$url")

        val request = Request.Builder()
            .get()
            .url(url)
            .build()

        return client.newCall(request)
    }
}