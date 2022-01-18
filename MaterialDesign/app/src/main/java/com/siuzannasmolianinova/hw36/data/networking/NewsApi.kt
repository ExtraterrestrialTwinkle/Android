package com.siuzannasmolianinova.hw36.data.networking

import com.siuzannasmolianinova.hw36.data.Country
import com.siuzannasmolianinova.hw36.data.db.Category
import com.siuzannasmolianinova.hw36.data.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    fun loadGlobalNews(
        @Query ("sources") source: String
    ): Call<Response>

    @GET("v2/top-headlines")
    fun loadNews(
        @Query("country") country: Country,
        @Query("category") category: Category
    ): Call<Response>
}