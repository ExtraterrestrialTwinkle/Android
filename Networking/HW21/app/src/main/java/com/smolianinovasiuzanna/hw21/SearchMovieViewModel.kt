package com.smolianinovasiuzanna.hw21

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.Call

class SearchMovieViewModel: ViewModel() {

    private var currentCall: Call? = null
    private val movieListLiveData = MutableLiveData<List<ImdbResponse.Movie>>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val errorMessageLiveData = MutableLiveData<String>()

    private val repository = Repository { error ->
        errorMessageLiveData.postValue(error.toString())
        Log.d("exception", error.error.toString())
    }

    val movieList: LiveData<List<ImdbResponse.Movie>>
        get() = movieListLiveData

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    val errorMessage: LiveData<String>
        get() = errorMessageLiveData

    fun search(title: String, year: String, type: String){
        isLoadingLiveData.postValue(true)
        currentCall = repository.searchMovie(title, year, type) { movies ->
            isLoadingLiveData.postValue(false)
            movieListLiveData.postValue(movies)
            currentCall = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentCall?.cancel()
    }

}