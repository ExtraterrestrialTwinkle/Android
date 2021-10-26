package com.skillbox.githubcoroutines.ui.repository_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.data.RepositoryListRepository
import kotlinx.coroutines.*
import okhttp3.internal.wait

class RepositoryListViewModel: ViewModel() {
    private val repository = RepositoryListRepository()
    private val userRepositoryListLiveData =
        MutableLiveData<List<GithubResponse.UserRepository>>(emptyList())
    private val loadingLiveData = MutableLiveData<Boolean>()

    val userRepository: LiveData<List<GithubResponse.UserRepository>>
        get() = userRepositoryListLiveData

    val isLoading: LiveData<Boolean>
        get() = loadingLiveData

    private val scope = CoroutineScope(Dispatchers.Main)

    fun showRepositories(){
        scope.launch(){
            try{
                var repositories: List<GithubResponse.UserRepository> = emptyList()
                val job = launch{
                    Log.d("job", "start")
                    repositories = repository.showUserRepositories()
                    Log.d("job", "showUserRepositories")
                }
                    job.join()
                if(job.isCompleted){
                    Log.d("job", "isComplete")
                    userRepositoryListLiveData.postValue(repositories)
                    Log.d("repositories", "$repositories")
                    loadingLiveData.postValue(false)
                } else {
                    Log.d("job", "notCompleted")
                    loadingLiveData.postValue(true)
                }
            } catch(t:Throwable){
                userRepositoryListLiveData.postValue(emptyList())
                loadingLiveData.postValue(false)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
