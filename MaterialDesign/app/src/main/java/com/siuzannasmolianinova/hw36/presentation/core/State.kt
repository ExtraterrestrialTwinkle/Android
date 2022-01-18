package com.siuzannasmolianinova.hw36.presentation.core

import com.siuzannasmolianinova.hw36.data.db.Article
import kotlinx.coroutines.flow.Flow

sealed class State {
    data class Loading(val isLoading: Boolean) : State()
    data class Success(val data: Flow<List<Article>>) : State()
    data class Error(val error: Throwable) : State()
}