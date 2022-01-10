package com.smolianinovasiuzanna.hw28.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.MediaStore.createFavoriteRequest
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.smolianinovasiuzanna.hw28.data.networking.Network
import com.smolianinovasiuzanna.hw28.utils.haveQ
import com.smolianinovasiuzanna.hw28.utils.haveR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class VideoRepository(private val context: Context) {

    private val observer: ContentObserver? = null
    private var directory: String? = Environment.DIRECTORY_MOVIES
    private var type: String? = null
    private var subtype: String? = null

    fun observeVideos( onChange: () -> Unit){
        val observer = object: ContentObserver(Handler(Looper.getMainLooper())){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                onChange()
            }
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true, // указывает, нужно ли наблюдать изменения по uri, который не в точности как переданный, а начинается с него
            observer
        )
    }

    fun unregisterObserver()= observer?.let{ context.contentResolver.unregisterContentObserver(it)}

    @SuppressLint("Range")
    suspend fun loadVideoList(selection: String?): List<Video> {

        val videoList = mutableListOf<Video>()
        var favorites = false

        withContext(Dispatchers.IO){
            context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            ) ?.use{ cursor ->
                while(cursor.moveToNext()){
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                    val size = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    if(haveR()){
                        favorites = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Video.Media.IS_FAVORITE)) == "1"
                      //  Log.d("REPO", "favorites: ${cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.IS_FAVORITE))}")
                    }
                    val video = Video(id = id, title = title, uri = uri, size = size)
                    video.favorite = favorites
                    videoList += video
                }
            }
        }
        return if(selection != null){
            videoList.filter { it.favorite }
        } else videoList
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun saveVideo(title: String, url: String) = withContext(Dispatchers.IO){
        val videoUri = saveVideoDetails(title, url)
        downloadVideo(url, videoUri)
        makeVideoFileVisible(videoUri)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun saveVideoDetails(title: String, url: String): Uri {
        val volume = if (haveQ()) {
            MediaStore.VOLUME_EXTERNAL_PRIMARY
        } else {
            MediaStore.VOLUME_EXTERNAL
        }
        val mimeType = getMimeType(url)
        if ( mimeType?.substring(0..5) != VIDEO_MIME_TYPE) {
            Log.e(" Repo", "Данный файл не является видео!")
            throw IllegalArgumentException("Данный файл не является видео!")
        }
        val videoCollectionUri = MediaStore.Video.Media.getContentUri(volume)
        val videoDetails = ContentValues().apply{
            put(MediaStore.Video.Media.DISPLAY_NAME, title)
            put(MediaStore.Video.Media.MIME_TYPE, mimeType)
            if (haveQ()) put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
            if (haveQ()) put(MediaStore.Images.Media.IS_PENDING, 1) // флаг, указывающий, что файл еще не скачан (1) и не виден в файловом менеджере
        }
        return context.contentResolver.insert(videoCollectionUri, videoDetails)!!
    }

    private suspend fun downloadVideo(url: String, videoUri: Uri) {
        context.contentResolver
            .openOutputStream(videoUri) ?.use{ outputStream ->
                try {
                    Network.api
                        .getFile(url)
                        .byteStream()
                        .use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                } catch (t: Throwable) {
                    val delete = deleteFile(uri = videoUri)
                    println(delete)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun makeVideoFileVisible(uri: Uri){
        if(haveQ().not()) return
        val videoDetails = ContentValues().apply{
            put(MediaStore.Video.Media.IS_PENDING, 0) // файл скачан и теперь доступен другим приложениям
        }
        context.contentResolver.update(uri, videoDetails, null, null)
    }

    suspend fun deleteVideo(id: Long) {
        withContext(Dispatchers.IO) {
            val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            context.contentResolver.delete(uri, null, null)
        }
    }

    private suspend fun getMimeType(url: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
        }
        if (type == null) {
            try {
                Network.api
                    .getFile(url).apply {
                        type = this.contentType()?.type
                        subtype = this.contentType()?.subtype
                        println("$type/$subtype")
                        if (type != "video") {
                            throw(Throwable("Данный файл не является видеофайлом!"))
                        }
                    }
            } catch (t: Throwable) {
                Log.e("Error in REPO!", "$t.message", t)
            }
        }
        return "$type/$subtype"
    }

    private fun deleteFile(uri: Uri): Boolean? = DocumentFile.fromSingleUri(context, uri)?.delete()

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun saveVideo(uri: Uri, url: String) {
        lateinit var file: File
        directory = uri.path.toString().substring(
                startIndex = uri.path.toString().lastIndexOf(':')+1,
                endIndex = uri.path.toString().lastIndexOf('/')
            )
        println(directory)
        val filename = uri.path.toString()
            .substring(startIndex = uri.path.toString().lastIndexOf('/') +1)

        //Файл появляется в списке приложения и в файловой системе устройства,
        // но его не видно в галерее
        withContext(Dispatchers.IO) {
            try {
                Network.api
                    .getFile(url).apply {
                        getMimeType(url)
                    }
                    .byteStream()
                    .use { inputStream: InputStream ->
                        file = File("$directory/$filename.$subtype")
                        println("$directory/$filename.$subtype")
                        inputStream.copyTo(file.outputStream())
                    }

            } catch (t: Throwable) {
                Log.e("Error in REPO", "${t.message}", t)
                val delete = deleteFile(uri = uri)
                println(delete)
            }
        }
    }

    suspend fun loadFavorites(flag: Boolean): List<Video>{

        return withContext(Dispatchers.IO) {
            if (haveR()) {
                if (flag) {
                    val selection = "${MediaStore.MediaColumns.IS_FAVORITE}=1"
                    loadVideoList(selection)
                }  else loadVideoList(null)
            } else throw Throwable("Данное действие недоступно")
        }

    }

// Работает только в одну сторону? Обратно из избранного не убирает
    suspend fun requestFavoriteMedia (media: List<Video>, state: Boolean): Boolean{
        Log.d("REPO", "Favorite media = $media, state = $state")
        if(haveR()) {
            var listUri = mutableListOf<Uri>()
            withContext(Dispatchers.IO) {
                media.map { video ->
                    context.contentResolver.query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            listUri.add(
                                ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                video.id
                                )
                            )
                        }
                        createFavoriteRequest(context.contentResolver, listUri, state)
                        video.favorite = state
                    }
                }
            }
            return state
        } else throw Throwable("Данное действие недоступно")
    }

    companion object{
        private const val VIDEO_MIME_TYPE = "video/"
    }
}