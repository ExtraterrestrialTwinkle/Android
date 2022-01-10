package com.siuzannasmolianinova.hw35.core

import android.content.Context
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.siuzannasmolianinova.hw35.core.Constants.DOWNLOAD_URL_KEY
import com.siuzannasmolianinova.hw35.data.networking.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

class DownloadWorker(
    private val context: Context,
    params: WorkerParameters
    ) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(DOWNLOAD_URL_KEY)
        if (url != null) {
            downloadFile(url) }
        return Result.success()
    }

    private suspend fun downloadFile(url: String) = withContext(Dispatchers.IO){
        Timber.d("downloadFile url = $url")
        val myFolder = context.getExternalFilesDir("test_folder")
        if (!(myFolder?.exists())!!){myFolder.mkdirs()}
        Timber.d("folder = ${myFolder.absolutePath}")
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@withContext
        var type = MimeTypeMap.getFileExtensionFromUrl(url)
        var name ="test_file.$type"
        val file = File(myFolder, name)
        Timber.d( "path = ${file.absolutePath}, name = ${file.name}")
        try{
            Network.api.downloadFile(url).apply{
                Timber.d("contentLength = ${this.contentLength()}")
                if(type == null) {
                    type = this.contentType()?.subtype
                    Timber.d("type = $type")
                    name = "test_file.$type"
                    val newFile = File(myFolder, name)
                    file.renameTo(newFile)
                    Timber.d("path = ${file.absolutePath}, name = ${file.name}")
                }
            }
                .byteStream()
                .use{inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
        } catch(t: Throwable){
            Timber.e(t)
            file.delete()
            throw t
        }
        //Проверка имени файла
        val directory = File(myFolder.absolutePath)
        directory.listFiles()?.forEach {
            if(it.isFile){
                Timber.d("file in folder ${it.absolutePath}")
            }
        }
    }
}