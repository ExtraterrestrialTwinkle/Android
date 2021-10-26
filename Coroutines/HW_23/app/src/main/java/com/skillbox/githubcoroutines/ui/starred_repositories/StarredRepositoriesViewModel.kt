package com.skillbox.githubcoroutines.ui.starred_repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.data.StarredRepositoriesRepository

class StarredRepositoriesViewModel: ViewModel() {
    private val repository = StarredRepositoriesRepository()

    private val starredRepositoryLiveData =
        MutableLiveData<List<GithubResponse.UserRepository>>(emptyList())

    val starredRepositoryList: LiveData<List<GithubResponse.UserRepository>>
        get() = starredRepositoryLiveData

    fun showStarred(){
        repository.showStarredRepositories(
            onComplete = {repositories ->
                starredRepositoryLiveData.postValue(repositories)
            },
            onError = { error ->
                starredRepositoryLiveData.postValue(emptyList())

            }
        )
    }
}