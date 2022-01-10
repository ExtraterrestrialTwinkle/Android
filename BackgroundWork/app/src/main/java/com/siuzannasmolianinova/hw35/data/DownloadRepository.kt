package com.siuzannasmolianinova.hw35.data

import android.content.Context
import androidx.work.*
import com.siuzannasmolianinova.hw35.core.Constants.DOWNLOAD_URL_KEY
import com.siuzannasmolianinova.hw35.core.Constants.DOWNLOAD_WORK_ID
import com.siuzannasmolianinova.hw35.core.DownloadWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DownloadRepository(private val context: Context) {
    fun startDownload(url: String){
        Timber.d("Start download")
        val workData = workDataOf(DOWNLOAD_URL_KEY to url)

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(DOWNLOAD_WORK_ID, ExistingWorkPolicy.KEEP, workRequest)
    }

    fun stopDownloading(){
        WorkManager.getInstance(context).cancelUniqueWork(DOWNLOAD_WORK_ID)
    }
}