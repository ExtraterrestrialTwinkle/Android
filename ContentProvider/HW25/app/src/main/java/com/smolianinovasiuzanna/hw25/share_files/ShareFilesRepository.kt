package com.smolianinovasiuzanna.hw25.share_files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log

import androidx.core.content.FileProvider
import com.smolianinovasiuzanna.hw24.network.Network
import com.smolianinovasiuzanna.hw25.BuildConfig
import com.smolianinovasiuzanna.hw25.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ShareFilesRepository(private val context: Context) {

    private val myFolder = context.getExternalFilesDir("my_downloads")
    private var filename: String? = null
    private val sharedPrefs = context.getSharedPreferences("download_prefs", Context.MODE_PRIVATE)

    // Функция, скачивающая в External Storage файлы по введенному url
    suspend fun downloadFileFromNetwork(url: String): Uri?{
        Log.d("Repo", "fun downloadFile")
        if (!(myFolder?.exists())!!){myFolder.mkdirs()}
        Log.d("myFolder", myFolder.absolutePath)
        val timestamp = System.currentTimeMillis()

        val name = url.substring(url.lastIndexOf('/') + 1)
        val file = File(myFolder, "${timestamp}_${name}")
        Log.d("Repo myFile", "path = ${file.absolutePath}, name = ${file.name}")
        filename = file.name

        if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) throw(Exception("Storage state exception"))
        return try{
            Network.api.getFile(url)
                .byteStream()
                .use {inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
           shareFile(file)
        } catch(t: Throwable){
            Log.e("Repo", "Error", t)
            file.delete()
            throw(t)
        }
    }

    // Сохраняет информацию о скачанных файлах в SharedPreferences
    fun saveInfoInSharedPrefs(url: String){
        Log.d("Repo", "fun saveInfoInSharedPrefs")
        sharedPrefs.edit()
            .putString(url, filename)
            .commit()
        println(sharedPrefs.all)
    }

    //Расшаривает файлы
    private suspend fun shareFile(file: File): Uri? {
        return withContext(Dispatchers.IO){
            Log.d("Repo", "shareFile")
            if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) null
            else{
                FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileProvider",
                    file
                )
            }
        }
    }

    fun startIntent(uri: Uri): Intent{
        Log.d("Fragment", "startIntent uri = $uri")
        val intent = Intent(Intent.ACTION_SEND).apply{
            putExtra(Intent.EXTRA_STREAM, uri)
            type = context.contentResolver.getType(uri)
            Log.d("type = ", "$type")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
       return Intent.createChooser(intent, null)
    }
}