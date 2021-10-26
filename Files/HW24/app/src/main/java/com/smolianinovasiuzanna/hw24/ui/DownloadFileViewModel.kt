package com.smolianinovasiuzanna.hw24.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw24.data.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DownloadFileViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(application)
    private val downloadLiveData = MutableLiveData<Unit>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()
    private val checkFileLiveData = MutableLiveData<Boolean>()

    val download: LiveData<Unit>
        get() = downloadLiveData

    val loading: LiveData<Boolean>
        get() = loadingLiveData

    val error: LiveData<String>
        get() = errorLiveData

    val checkFile: LiveData<Boolean>
        get() = checkFileLiveData

    private val scope = viewModelScope
    private val errorHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        Log.e("Coroutine exception", "${exception.message}")
        errorLiveData.postValue(exception.message)
    }

    fun downloadFile(url: String) {
        scope.launch(Dispatchers.IO + errorHandler) {
            launch()  {
                repository.downloadFile(url)
                loadingLiveData.postValue(true)
                delay(2000)// чтобы увидеть лоадер
                Log.d("ViewModel", "fun downloadFile: file is downloading")
            }.join()
            Log.d("ViewModel", "fun downloadFile: file downloaded")
            loadingLiveData.postValue(false)
            downloadLiveData.postValue(repository.saveInfoInSharedPrefs(url))
        }
    }

    fun checkUrlName(url: String) {
        Log.d("ViewModel", "fun checkUrlName")
        var isExist: Boolean = false
        scope.launch(Dispatchers.IO + errorHandler) {
            launch {
                isExist = repository.getInfoFromSharedPrefs(url)
            }.join()
            checkFileLiveData.postValue(isExist)
            Log.d("ViewModel", "fun checkUrl: $isExist")
        }
    }

    fun checkFirstLaunch(context: Context){
        scope.launch(Dispatchers.IO + errorHandler){
            repository.checkFirstTimeLaunch(context)
        }
    }
}
