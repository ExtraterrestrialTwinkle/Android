package com.smolianinovasiuzanna.hw33.features.download_progress

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.smolianinovasiuzanna.hw33.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.properties.Delegates
import okhttp3.ResponseBody

import android.widget.Toast

import java.io.*
import kotlin.math.pow
import kotlin.math.roundToInt
import android.os.Parcel

import android.os.Parcelable
import android.os.Parcelable.Creator
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer


class ProgressRepository(private val context: Context) {
    var titleValue: String? = null
    var maxProgress: Long? = null
    var downloadProgress: Long? = null

    suspend fun downloadFile(): Boolean {
//        withContext(Dispatchers.IO) {
//            Timber.d("download")
//            try{
//                val request = DownloadManager.Request(Uri.parse(testUrl))
//                request.setDescription("Download file")
//                request.setTitle("Download")
//                val type = getMimeType(testUrl)
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                request.setDestinationInExternalPublicDir(
//                    Environment.DIRECTORY_DOWNLOADS,
//                    "test_file.$type"
//                )
//                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                downloadManager.enqueue(request)
//
//            } catch(t: Throwable){
//                Timber.e( "Error download", t)
//                throw(t)
//            }
//        }


        withContext(Dispatchers.IO){
            Timber.d( "download")
            val filesDir = context.getExternalFilesDir("folder")
            val type = getMimeType(testUrl)
            val fileName = "test_file.$type"
            val file = File(filesDir, fileName)
            if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@withContext

            val progressListener: ProgressListener = object : ProgressListener {
                var firstUpdate = true

                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    if (done) {
                        Timber.d("completed")
                    } else {
                        if (firstUpdate) {
                            firstUpdate = false
                            if (contentLength == -1L) {
                                Timber.d("content-length: unknown")
                                title("content-length: unknown")

                            } else {
                                Timber.d("content-length: %d\n", contentLength)
                                title("content-length: $contentLength")
                                maxProgress(contentLength)
                            }
                        }
                        Timber.d("$bytesRead")
                        if (contentLength != -1L) {
                            Timber.d("%d%% done\n", 100 * bytesRead / contentLength)
                            downloadProgress(100 * bytesRead / contentLength)
                        }
                    }
                }
            }
            try{
                Network.api.downloadFile(testUrl)
                    .apply {
                        var bufferedSource: BufferedSource? = null
                        val contentLength = this.contentLength()
                        if (bufferedSource == null) {
                            bufferedSource = this.source().buffer
                        }
                        object : ForwardingSource(bufferedSource) {
                            var totalBytesRead = 0L

                            override fun read(sink: Buffer, byteCount: Long): Long {
                                val bytesRead = super.read(sink, byteCount)
                                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                                progressListener.update(
                                    totalBytesRead,
                                    contentLength,
                                    bytesRead == -1L
                                )
                                return bytesRead
                            }
                        }
                    }
                    .byteStream()
                    .use{ inputStream ->
                        inputStream.copyTo(file.outputStream())
                    }
                Timber.d( file.absolutePath)
            } catch(t: Throwable){
                Timber.e( "Error download", t)
                file.delete()
                throw(t)
            }
        }
        return false
    }

    fun title(value: String) {
        titleValue = value
        Timber.d(titleValue)
    }

    fun maxProgress(value: Long) {
        maxProgress = value
        Timber.d(maxProgress.toString())
    }

    fun downloadProgress(value: Long) {
        downloadProgress = value
        Timber.d(downloadProgress.toString())
    }

    private suspend fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        Timber.d("ext = $extension")
        if (extension != null) {
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            type = mimeType?.substringAfter('/')
        }
        if (type == null) {
            try {
                Network.api.downloadFile(url).apply {
                        type = this.contentType()?.subtype
                        Timber.d(type)
                    }
            } catch (t: Throwable) {
                Timber.e("$t.message", t)
            }
        }
        Timber.d("mimeType = $type")
        return type
    }

    interface ProgressListener{
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }

    companion object{
        private const val testUrl =
           "https://dropmefiles.net/ru/X3Vy4AU"
          //  "https://ak.picdn.net/shutterstock/videos/1067634035/preview/stock-footage-intelligent-vehicles-cars-communicating-ai-logistic-autonomous-delivery-vehicles-iot-gps-tracking.webm"
        // "https://media.istockphoto.com/videos/koi-fish-video-id1177150446"
        // "https://ak.picdn.net/shutterstock/videos/1055153270/preview/stock-footage-modern-technological-cnc-cutting-power-action-on-metallic-horizontal-ironwork-object-hot-gas.webm"
    }

}