package com.skillbox.multithreading.threading

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.multithreading.networking.Movie
import com.skillbox.multithreading.networking.Network

class MovieViewModel: ViewModel() {
    private val repository = MovieRepository()
    private val moviesLiveData = MutableLiveData<List<Movie>>()

    val movies: LiveData<List<Movie>>
        get() = moviesLiveData

    private val movieIds = listOf<String>(
        "tt0111161",
        "tt0068646"
    )

    fun requestMovies() {
        Log.d("ThreadTest", "requestMovies start on ${Thread.currentThread().name}")
        repository.fetchMovies(movieIds) { movies ->
            Log.d("ThreadTest", "requestMovies fetched on ${Thread.currentThread().name}")
            moviesLiveData.postValue(movies)
        }
        Log.d("ThreadTest", "requestMovies end on ${Thread.currentThread().name}")

    }
}