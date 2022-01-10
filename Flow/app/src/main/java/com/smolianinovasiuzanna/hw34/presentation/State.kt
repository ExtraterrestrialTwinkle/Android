package com.smolianinovasiuzanna.hw34.presentation

import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import kotlinx.coroutines.flow.Flow

sealed class State {
    data class Loading(val isLoading: Boolean) : State()
    data class Success(val data: Flow<List<Movie>>) : State()
    data class Error(val error: Throwable) : State()
}