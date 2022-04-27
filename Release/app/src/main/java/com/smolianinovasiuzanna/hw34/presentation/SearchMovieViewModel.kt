package com.smolianinovasiuzanna.hw34.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw34.data.movie_database.MovieType
import com.smolianinovasiuzanna.hw34.data.MovieRepository
import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import timber.log.Timber

class SearchMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository(application)
    private val scope = viewModelScope
    private lateinit var searchMovieJob: Job

    private var _loadingState = MutableStateFlow<State>(State.Loading(false))
    val loadingState: StateFlow<State> = _loadingState

    fun bind(queryFlow: Flow<String>, movieTypeFlow: Flow<MovieType>) {
        Timber.d("Start searching job")
        searchMovieJob = combine(
            queryFlow,
            movieTypeFlow
        ) { query, movieType ->
            query to movieType
        }
            .debounce(500)
            .distinctUntilChanged()
            .filter { it.first.length > 3 }
            .mapLatest {
                _loadingState.value = State.Loading(true)
                try {
                    _loadingState.value =
                        State.Success(repository.searchMovie(it.first, it.second))
                } catch (t: Throwable) {
                    Timber.e(t)
                    _loadingState.value = State.Error(t)
                } finally {
                    _loadingState.value = State.Loading(false)
                }
            }
            .launchIn(scope)
    }

    fun cancelSearchingJob() {
        Timber.d("End searching job")
        searchMovieJob.cancel()
    }
}