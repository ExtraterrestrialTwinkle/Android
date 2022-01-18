package com.siuzannasmolianinova.hw36.presentation.view_model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.siuzannasmolianinova.hw36.data.Country
import com.siuzannasmolianinova.hw36.data.Repository
import com.siuzannasmolianinova.hw36.data.db.Category
import com.siuzannasmolianinova.hw36.presentation.core.ConnectionState
import com.siuzannasmolianinova.hw36.presentation.core.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseNewsViewModel(val app: Application): AndroidViewModel(app) {

    private val repository = Repository()
    private val scope = viewModelScope
    private lateinit var loadListJob: Job

    private var _loadingState = MutableStateFlow<State>(State.Loading(false))
    val loadingState: StateFlow<State> = _loadingState

    private var _connectionState = MutableStateFlow(ConnectionState.CONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    @RequiresApi(Build.VERSION_CODES.O)
    fun initLoading(category: Flow<Category>, country: Flow<Country>, isConnected: Boolean) {
        Timber.d("Start loadList job")
        loadListJob = combine(
            category,
            country
        ) { category, country ->
            category to country
        }
            .debounce(500)
            .distinctUntilChanged()
            .mapLatest {
                _loadingState.value = State.Loading(true)
                try {
                    if(!isConnected) {
                        Timber.d("checkConnection = disconnected")
                        _loadingState.value =
                            State.Success( repository.readFromDB(it.first, it.second) )
                    } else {
                        Timber.d("checkConnection = connected")
                        _loadingState.value =
                            State.Success(repository.initLoading(it.first, it.second))
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                    _loadingState.value = State.Error(t)
                } finally {
                    _loadingState.value = State.Loading(false)
                }
            }
            .launchIn(scope)
    }

    fun checkConnection(): Boolean{
        return if(repository.isOnline(app)){
            _connectionState.value = ConnectionState.CONNECTED
            Timber.d("connected")
            true
        }
        else{
            _connectionState.value = ConnectionState.UNKNOWN
            _connectionState.value = ConnectionState.DISCONNECTED
            Timber.d("disconnected")
            false
        }
    }

    fun cancelJob() {
        Timber.d("End loadList job")
        loadListJob.cancel()
    }
}