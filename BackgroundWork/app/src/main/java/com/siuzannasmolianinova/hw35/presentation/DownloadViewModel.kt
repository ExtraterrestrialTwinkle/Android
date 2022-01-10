package com.siuzannasmolianinova.hw35.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.siuzannasmolianinova.hw35.data.DownloadRepository
import kotlinx.coroutines.launch

class DownloadViewModel (app: Application) : AndroidViewModel(app) {
    private val repository = DownloadRepository(app)

    fun downloadFile(url: String){
        viewModelScope.launch{
            repository.startDownload(url)
        }
    }

    fun stopDownloading(){
        repository.stopDownloading()
    }
}