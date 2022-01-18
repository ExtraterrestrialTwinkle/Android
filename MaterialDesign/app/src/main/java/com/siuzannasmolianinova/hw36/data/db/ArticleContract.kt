package com.siuzannasmolianinova.hw36.data.db

object ArticleContract {

    const val TABLE_NAME = "articles"

    object Columns{
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IMAGE_LINK = "image_link"
        const val COUNTRY = "country"
        const val CATEGORY = "category"
    }
}