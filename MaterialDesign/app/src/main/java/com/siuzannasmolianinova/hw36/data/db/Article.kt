package com.siuzannasmolianinova.hw36.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.siuzannasmolianinova.hw36.data.Country
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = ArticleContract.TABLE_NAME)
data class Article(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ArticleContract.Columns.ID)
    val id: Long = 0L,
    @Json(name = "title")
    @ColumnInfo(name = ArticleContract.Columns.TITLE)
    val title: String,
    @Json(name = "description")
    @ColumnInfo(name = ArticleContract.Columns.DESCRIPTION)
    val description: String,
    @Json(name = "urlToImage")
    @ColumnInfo(name = ArticleContract.Columns.IMAGE_LINK)
    val imageLink: String?,
    @ColumnInfo(name = ArticleContract.Columns.COUNTRY)
    val country: Country = Country.RUSSIA,
    @ColumnInfo(name = ArticleContract.Columns.CATEGORY)
    val category: Category = Category.GLOBAL
){
    override fun toString(): String {
        return title
    }

    override fun equals(other: Any?): Boolean {
        return (other as Article).run {
            this.hashCode() == other.hashCode()
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (imageLink?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + category.hashCode()
        return result
    }
}
