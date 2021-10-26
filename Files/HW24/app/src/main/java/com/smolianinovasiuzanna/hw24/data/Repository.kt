package com.smolianinovasiuzanna.hw24.data

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.util.Log
import com.smolianinovasiuzanna.hw24.data.Values.SHARED_PREFS_NAME
import com.smolianinovasiuzanna.hw24.network.Network
import java.io.File
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentResolverCompat.query
import androidx.fragment.app.FragmentActivity
import com.smolianinovasiuzanna.hw24.ui.DownloadFileFragment
import java.io.FileNotFoundException
import java.nio.file.Paths


class Repository(val context: Context) {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    private val myFolder = context.getExternalFilesDir("myFolder")
    private var filename: String? = null

    // Функция, скачивающая в External Storage файлы по введенному url
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun downloadFile(url: String){
        Log.d("Repo", "fun downloadFile")
        if (!(myFolder?.exists())!!){myFolder.mkdirs()}
        Log.d("myFolder", myFolder.absolutePath)
        val timestamp = System.currentTimeMillis()

        val name = url.substring(url.lastIndexOf('/') + 1)
        val file = File(myFolder, "${timestamp}_${name}")
        Log.d("myFile", "path = ${file.absolutePath}, name = ${file.name}")
        filename = file.name

        if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return
        try{
            Network.api.getFile(url)
                .byteStream()
                .use{inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
        } catch(t: Throwable){
            Log.e("Repo", "Error", t)
            file.delete()
            throw(t)
        }
        //Проверка имени файла
        val directory = File(myFolder.absolutePath)
        directory.listFiles()?.forEach {
            if(it.isFile){
                Log.d("folderFiles", "file in folder ${it.absolutePath}")
            }
        }
    }

// Функция, определяющая запущена ли программа в первый раз и запускающая скачивание файлов
    suspend fun checkFirstTimeLaunch(context: Context){
        if(sharedPrefs.getBoolean("first_time", true)){
            Log.d("first time launch", "")
            try{
                context.resources.assets.open("links.txt")
                    .bufferedReader()
                    .use {
                        it.readLines().forEach { link ->
                            download(link, context)
                        }
                    }
            } catch(t: Throwable){
                Log.e("Repo", "Error firstTimeLaunch", t)
            }
        }
        sharedPrefs.edit()
            .putBoolean("first_time", false)
            .apply()
    }

    // Функция, скачивающая при первом запуске программы файлы из links.txt в Internal Storage
    private suspend fun download(url: String, context: Context){
        Log.d("Repo", "fun download")
        val filesDir = context.filesDir
        val fileName = url.substring(url.lastIndexOf('/'))
        val file = File(filesDir, fileName)
        try{
            Network.api.getFile(url)
                .byteStream()
                .use{ inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
            Log.d("Downloaded file", file.absolutePath)
        } catch(t: Throwable){
                Log.e("Repo", "Error download", t)
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

    //Функция, определяющая скачивался ли файл по данному url
    fun getInfoFromSharedPrefs(url: String): Boolean{
        Log.d("Repo", "fun getInfoFromSharedPrefs")
        return sharedPrefs.getString(url, null) != null
    }

}