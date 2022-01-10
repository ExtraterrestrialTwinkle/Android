package com.smolianinovasiuzanna.hw28

import android.app.Application
import android.app.RecoverableSecurityException
import android.app.RemoteAction
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw28.data.Video
import com.smolianinovasiuzanna.hw28.data.VideoRepository
import com.smolianinovasiuzanna.hw28.utils.SingleLiveEvent
import com.smolianinovasiuzanna.hw28.utils.haveQ
import kotlinx.coroutines.launch

class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VideoRepository(application)

    private val videoListLiveData = MutableLiveData<List<Video>>()
    private val errorSingleLiveEvent = SingleLiveEvent<Throwable>()
    private val permissionsGrantedLiveData = MutableLiveData(true)
    private val recoverableDeleteActionLiveData = MutableLiveData<RemoteAction>()
    private val recoverableFavoriteActionLiveData = MutableLiveData<RemoteAction>()
    private val saveSuccessSingleLiveEvent = SingleLiveEvent<Unit>()
    private val loadingLiveData = SingleLiveEvent<Boolean>()
    private val favoriteLiveData = MutableLiveData<Boolean>()

    val videoList: LiveData<List<Video>>
        get() = videoListLiveData

    val error: LiveData<Throwable>
        get() = errorSingleLiveEvent

    val permissionsGranted: LiveData<Boolean>
        get() = permissionsGrantedLiveData

    val recoverableDeleteAction: LiveData<RemoteAction>
        get() = recoverableDeleteActionLiveData

    val recoverableFavoriteAction: LiveData<RemoteAction>
        get() = recoverableFavoriteActionLiveData

    val saveSuccess: LiveData<Unit>
        get() = saveSuccessSingleLiveEvent

    val loading: LiveData<Boolean>
        get() = loadingLiveData

    val favorite: LiveData<Boolean>
        get() = favoriteLiveData

    private var isObservingStarted: Boolean = false
    private var pendingDeleteId: Long? = null
    private var pendingFavoriteStateAndMedia: Map<Boolean, List<Video>>? = null

    fun updatePermissionState(isGranted: Boolean) {
        if(isGranted) {
            permissionsGranted()
        } else {
            permissionsDenied()
        }
    }

    fun permissionsGranted() {
        loadList()
        if(isObservingStarted.not()) {
            repository.observeVideos{ loadList() }
            isObservingStarted = true
        }
        permissionsGrantedLiveData.postValue(true)
    }

    fun permissionsDenied() {
        permissionsGrantedLiveData.postValue(false)
    }

    private fun loadList(){
        viewModelScope.launch {
            try {
                videoListLiveData.postValue(repository.loadVideoList(null))
            } catch (t: Throwable) {
                errorSingleLiveEvent.postValue(t)
                videoListLiveData.postValue(emptyList())
            }
        }
    }

    fun deleteVideoById(id: Long){
        viewModelScope.launch {
            try {
                repository.deleteVideo(id)
                loadList()
                pendingDeleteId = null
            } catch (t: Throwable) {
                if(haveQ() && t is RecoverableSecurityException) {
                    pendingDeleteId = id
                    recoverableDeleteActionLiveData.postValue(t.userAction)
                } else {
                    errorSingleLiveEvent.postValue(t)
                }
            }
        }
    }

    fun confirmDelete() {
        pendingDeleteId?.let {
            deleteVideoById(it)
        }
    }

    fun declineDelete() {
        pendingDeleteId = null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadVideo(title: String, url: String){
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            try {
                repository.saveVideo(title, url)
                saveSuccessSingleLiveEvent.postValue(Unit)
                loadList()
            } catch (t: Throwable) {
                errorSingleLiveEvent.postValue(t)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadVideoToCustomDirectory(uri: Uri, url: String) {
        viewModelScope.launch {
            loadingLiveData.postValue(true)
            try {
                repository.saveVideo(uri, url)
                saveSuccessSingleLiveEvent.postValue(Unit)
                loadList()
            }catch (t: Throwable){
                errorSingleLiveEvent.postValue(t)
            }finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun requestFavoriteMedia (media: List<Video>, state: Boolean){
        viewModelScope.launch {
            try{
                favoriteLiveData.postValue(repository.requestFavoriteMedia(media, state)) // ливдату
                Log.d("VM", "Favorite media = $media, state = $state")
            } catch(t: Throwable){
                if(haveQ() && t is RecoverableSecurityException) {
                    pendingFavoriteStateAndMedia = mapOf(Pair(state, media))
                    recoverableFavoriteActionLiveData.postValue(t.userAction)
                } else {
                    errorSingleLiveEvent.postValue(t)
                }
            }
        }
    }

    fun confirmFavorite() {
        pendingFavoriteStateAndMedia?.let { args ->
            args.map { requestFavoriteMedia(it.value, it.key) }
        }
    }

    fun declineFavorite() {
        pendingFavoriteStateAndMedia = null
    }

    fun loadFavorites(flag: Boolean){
        viewModelScope.launch {
            val mediaList = repository.loadFavorites(flag)
            videoListLiveData.postValue(mediaList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.unregisterObserver()
    }
}