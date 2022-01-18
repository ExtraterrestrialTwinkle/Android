package com.siuzannasmolianinova.hw36.data

import com.siuzannasmolianinova.hw36.data.db.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Response(
    @Json(name = "status")
    val status: String?,
    @Json (name = "articles")
    val articles: List<Article>
)




