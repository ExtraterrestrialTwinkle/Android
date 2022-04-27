package com.smolianinovasiuzanna.hw34.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.smolianinovasiuzanna.hw34.data.movie_database.Database
import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import com.smolianinovasiuzanna.hw34.data.movie_database.MovieType
import com.smolianinovasiuzanna.hw34.data.movie_database.NestedObject
import com.smolianinovasiuzanna.hw34.data.networking.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MovieRepository(private val context: Context) {
    private val movieDao = Database.instance.movieDao()

    suspend fun searchMovie(query: String, movieType: MovieType): Flow<List<Movie>> {
        return if (!isOnline(context)) {
            Timber.e("Network connection error!")
            readFromDB(query, movieType)
                .flowOn(Dispatchers.IO)
        } else {
               searchFromNetwork(query, movieType)
                   .apply {
                       collect {
                           withContext(Dispatchers.IO){ movieDao.write(it) } }
                   }
        }
    }

    fun loadAllMoviesFromDB(): Flow<List<Movie>> = movieDao.readAll()

    private fun readFromDB(query: String, movieType: MovieType): Flow<List<Movie>> =
        movieDao.read(query, movieType)

    private suspend fun searchFromNetwork(query: String, movieType: MovieType): Flow<List<Movie>> =
        withContext(Dispatchers.IO) {
            Timber.d("$query, $movieType")
            suspendCancellableCoroutine { continuation ->
            Network.api.searchMovie(query, movieType)
                .enqueue(object : Callback<NestedObject> {
                    override fun onResponse(
                        call: Call<NestedObject>,
                        response: Response<NestedObject>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("isSuccessful")
                            response.body()?.search?.let{
                                flowOf(it)
                            }?.let {
                                continuation.resume(it) }
                        } else {
                            Timber.e("error: not Successful")
                            continuation.resumeWithException(IOException("Response is not successful"))
                        }
                    }
                    override fun onFailure(
                        call: Call<NestedObject>,
                        t: Throwable
                    ) {
                        Timber.e(t)
                        continuation.resumeWithException(t)
                    }
                })
        }
    }

    private fun isOnline(context: Context): Boolean {
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