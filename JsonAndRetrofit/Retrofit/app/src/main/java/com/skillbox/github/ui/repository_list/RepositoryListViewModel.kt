package com.skillbox.github.ui.repository_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.GithubResponse
import com.skillbox.github.data.RepositoryListRepository

class RepositoryListViewModel: ViewModel() {
    private val repository = RepositoryListRepository()
    private val userRepositoryListLiveData =
        MutableLiveData<List<GithubResponse.UserRepository>>(emptyList())

    val userRepository: LiveData<List<GithubResponse.UserRepository>>
        get() = userRepositoryListLiveData

    fun showRepositories(){
        repository.showUserRepositories(
            onComplete = {repositories ->
                userRepositoryListLiveData.postValue(repositories)
                Log.d("showRepositories", "onComplete")
            },
            onError = {throwable ->
                userRepositoryListLiveData.postValue(emptyList())
            }
        )
    }
}