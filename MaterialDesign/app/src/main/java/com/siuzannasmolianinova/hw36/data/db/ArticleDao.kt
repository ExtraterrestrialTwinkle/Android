package com.siuzannasmolianinova.hw36.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.siuzannasmolianinova.hw36.data.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun write(articleList: List<Article>)

    @Query("SELECT * FROM ${ArticleContract.TABLE_NAME} WHERE ${ArticleContract.Columns.COUNTRY} = :country AND ${ArticleContract.Columns.CATEGORY} = :category ORDER BY ${ArticleContract.Columns.ID} DESC")
    fun read(category: Category, country: Country): Flow<List<Article>>

}