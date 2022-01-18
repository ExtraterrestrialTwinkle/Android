package com.siuzannasmolianinova.hw36.data.db.type_converters

import androidx.room.TypeConverter
import com.siuzannasmolianinova.hw36.data.db.Category
import timber.log.Timber

class CategoryConverter {
    @TypeConverter
    fun convertCategoryToString(category: Category) = category.toString().apply{
        Timber.d(category.toString())
    }

    @TypeConverter
    fun convertStringToCategory(categoryString: String) = Category.valueOf(categoryString).apply{
        Timber.d("${Category.valueOf(categoryString)}")
    }
}