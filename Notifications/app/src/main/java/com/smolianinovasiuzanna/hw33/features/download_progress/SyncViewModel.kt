package com.smolianinovasiuzanna.hw33.features.download_progress

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.smolianinovasiuzanna.hw33.data.Repository
import com.smolianinovasiuzanna.hw33.features.download_progress.ProgressNotifications
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncViewModel(application: Application) : AndroidViewModel(application) {
     private val repository = ProgressRepository(application)
     private val scope = viewModelScope

     private val preparingLiveData = MutableLiveData<Boolean>()
     private val downloadLiveData = MutableLiveData<Boolean>()
     private val errorLiveData = MutableLiveData<Throwable>()
     private val progressLiveData = MutableLiveData<Long?>()
     private val maxProgressLiveData = MutableLiveData<Long?>()

     val preparing: LiveData<Boolean>
          get() = preparingLiveData

     val download: LiveData<Boolean>
          get() = downloadLiveData

     val error: LiveData<Throwable>
          get() = errorLiveData

     val progress: LiveData<Long?>
          get() = progressLiveData

     val maxProgress: LiveData<Long?>
          get() = maxProgressLiveData

     fun synchronize(){
          scope.launch {
               preparingLiveData.postValue(true)
               delay(2000)
               try{
                    preparingLiveData.postValue(false)
                    downloadLiveData.postValue(true)
                    maxProgressLiveData.postValue(repository.maxProgress)
                    progressLiveData.postValue(repository.downloadProgress)
                    downloadLiveData.postValue(repository.downloadFile())
               } catch(t: Throwable){
                    errorLiveData.postValue(t)
                    downloadLiveData.postValue(false)
               }
          }
     }

     fun synchronizeWithoutDownloading(maxProgress: Long){
          scope.launch{
               preparingLiveData.postValue(true)
               delay(2000)
               try{
                    preparingLiveData.postValue(false)
                    downloadLiveData.postValue(true)
                    (0 .. maxProgress step 10).forEach { progress ->
                         progressLiveData.postValue(progress)
                         delay(500)
                    }
                    downloadLiveData.postValue(false)
               } catch(t: Throwable){
                    errorLiveData.postValue(t)
                    downloadLiveData.postValue(false)
               }
          }
     }
}