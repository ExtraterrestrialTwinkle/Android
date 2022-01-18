package com.siuzannasmolianinova.hw36.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.siuzannasmolianinova.hw36.data.db.Article
import com.siuzannasmolianinova.hw36.data.db.Category
import com.siuzannasmolianinova.hw36.data.db.Database
import com.siuzannasmolianinova.hw36.data.networking.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Repository {

    private val articleDao = Database.instance.articleDao()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun initLoading(category: Category, country: Country?): Flow<List<Article>> {
        return when (category) {
            Category.GLOBAL -> loadNewsFeed( source = "rt")
            else -> loadNewsFeed(category, country!!)
        }
            .apply {
                collect {
                    withContext(Dispatchers.IO) { articleDao.write(it) }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadNewsFeed(source: String): Flow<List<Article>> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            Network.api.loadGlobalNews(source).enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("isSuccessful")
                        if(response.body()?.status == "ok") {
                            response.body()?.articles?.let {
                                flowOf(it)
                            }?.let {
                                continuation.resume(it)
                            }
                        } else {
                            Timber.e("error: ${response.body()?.status}")
                            continuation.resumeWithException(IllegalArgumentException("Unknown request"))
                        }
                    } else {
                        Timber.e("error: not Successful")
                        continuation.resumeWithException(IOException("Response is not successful"))
                    }
                }
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Timber.e(t)
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun loadNewsFeed(
        category: Category,
        country: Country
    ): Flow<List<Article>> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            Network.api.loadNews(country, category).enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("isSuccessful")
                        if(response.body()?.status == ResponseStatus.OK.name) {
                            response.body()?.articles?.let {
                                flowOf(it)
                            }?.let {
                                continuation.resume(it)
                            }
                        } else {
                            Timber.e("error: ${response.body()?.status}")
                            continuation.resumeWithException(IllegalArgumentException("Unknown request"))
                        }
                    } else {
                        Timber.e("error: not Successful")
                        continuation.resumeWithException(IOException("Response is not successful"))
                    }
                }
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Timber.e(t)
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    fun readFromDB(category: Category, country: Country): Flow<List<Article>>  =
        articleDao.read(category, country).apply{ Timber.d("readFromDB") }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Timber.i("NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}