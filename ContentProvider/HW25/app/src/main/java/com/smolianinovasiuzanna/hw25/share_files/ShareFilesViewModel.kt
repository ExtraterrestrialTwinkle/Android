package com.smolianinovasiuzanna.hw25.share_files

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ShareFilesViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ShareFilesRepository(application)
    private val downloadLiveData = MutableLiveData<Unit>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()
    private val uriLiveData = MutableLiveData<Uri?>()

    val download: LiveData<Unit>
        get() = downloadLiveData

    val loading: LiveData<Boolean>
        get() = loadingLiveData

    val error: LiveData<String>
        get() = errorLiveData

    val fileUri: LiveData<Uri?>
        get() = uriLiveData


    private val scope = viewModelScope
    private val errorHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        Log.e("Coroutine exception", "${exception.message}")
        errorLiveData.postValue(exception.message)
    }

    fun downloadAndShareFile(url: String) {
         scope.launch(Dispatchers.IO + errorHandler) {
             launch()  {
                 val uri = repository.downloadFileFromNetwork(url)
                 uriLiveData.postValue(uri)
                 loadingLiveData.postValue(true)
                 delay(2000)// чтобы увидеть лоадер
                 Log.d("ViewModel", "fun downloadFile: file is downloading")
             }.join()
             Log.d("ViewModel", "fun downloadFile: file downloaded")
             loadingLiveData.postValue(false)
             downloadLiveData.postValue(repository.saveInfoInSharedPrefs(url))
        }
    }
    fun startIntent(uri: Uri): Intent {
       return repository.startIntent(uri)
    }
}