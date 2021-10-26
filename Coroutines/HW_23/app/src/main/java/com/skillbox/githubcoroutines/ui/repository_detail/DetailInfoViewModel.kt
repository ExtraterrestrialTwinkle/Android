package com.skillbox.githubcoroutines.ui.repository_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.githubcoroutines.data.DetailInfoRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailInfoViewModel(
    private val owner: String,
    private val repositoryName: String,
    private val description: String
): ViewModel() {

    private val repository = DetailInfoRepository(
        owner,
        repositoryName
    )
    private val isStarredLiveData = MutableLiveData<Boolean>()
    val isStarredRepos: LiveData<Boolean>
        get() = isStarredLiveData

    val scope = viewModelScope

    fun showIsStarred(){
        Log.d("DIVM", "showIsStarred")
        scope.launch {
            try{
                val info = repository.showInfo()
                isStarredLiveData.postValue(info)
                Log.d("DIVM", "showInfo")
            } catch(t: Throwable){
                Log.e("ShowIsStarred Error", "${t.message}")
            }
        }
    }

    fun giveStar(){
        scope.launch{
            try{
                Log.d("GiveStar OK", "true")
                isStarredLiveData.postValue(repository.giveStar())
            }catch(t: Throwable){
                Log.e("GiveStar Error", "${t.message}")
            }
        }
    }

    fun deleteStar(){
        scope.launch{
            try{
                Log.d("DeleteStar OK", "false")
                isStarredLiveData.postValue(repository.deleteStar())
            }catch(t: Throwable){
                Log.e("DeleteStar Error", "${t.message}")
            }
        }

    }

}