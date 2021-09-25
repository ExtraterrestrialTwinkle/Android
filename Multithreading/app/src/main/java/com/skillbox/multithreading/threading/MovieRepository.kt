package com.skillbox.multithreading.threading

import android.util.Log
import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network.getMovieById
import okhttp3.internal.wait
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MovieRepository {
    private val executor = ThreadPoolExecutor(
        2,
        8,
        1000,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue()
    )

    fun fetchMovies(
        movieIds: List<String>,
        onMoviesFetched: (movies: List<Movie>) -> Unit
    ) {
        val allMovies = Collections.synchronizedList(mutableListOf<Movie>())
                    movieIds.chunked(1).map { movieChunk ->
                        val movies = Callable {
                            movieChunk.mapNotNull { movieId ->
                                getMovieById(movieId)
                            }
                        }
                        executor.submit(movies)
                    }.forEach{
                        allMovies.addAll(it.get())
                    }
            onMoviesFetched(allMovies)
            Log.d("ThreadTest", "fetchMovies end on ${Thread.currentThread().name}")

    }
}

