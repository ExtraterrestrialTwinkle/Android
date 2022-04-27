package com.smolianinovasiuzanna.hw34.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw34.data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import timber.log.Timber

class MovieListViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MovieRepository(application)
    private val scope = viewModelScope
    private lateinit var loadListJob: Job

    private var _loadingState = MutableStateFlow<State>(State.Loading(false))
    val loadingState: StateFlow<State> = _loadingState

    fun loadList(){
        Timber.d("Start loadList job")
        loadListJob = repository.loadAllMoviesFromDB()
            .flowOn(Dispatchers.IO)
            .map{
                try {
                    _loadingState.value =
                        State.Success(repository.loadAllMoviesFromDB())
                } catch (t: Throwable) {
                    Timber.e(t)
                    _loadingState.value = State.Error(t)
                }
            }
            .launchIn(scope)
    }

    fun cancelMovieJob() {
        Timber.d("End loadList job")
        loadListJob.cancel()
    }
}