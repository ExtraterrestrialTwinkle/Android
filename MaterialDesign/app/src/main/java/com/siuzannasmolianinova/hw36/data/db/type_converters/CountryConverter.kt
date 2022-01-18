package com.siuzannasmolianinova.hw36.data.db.type_converters

import androidx.room.TypeConverter
import com.siuzannasmolianinova.hw36.data.Country
import timber.log.Timber

class CountryConverter {
    @TypeConverter
    fun convertCountryToString(country: Country) = country.name.apply{
        Timber.d(country.name)
    }

    @TypeConverter
    fun convertStringToCountry(countryString: String) = Country.valueOf(countryString).apply{
        Timber.d("${Country.valueOf(countryString)}")
    }
}