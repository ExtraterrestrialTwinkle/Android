package com.skillbox.github.ui.starred_repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.GithubResponse
import com.skillbox.github.data.StarredRepositoriesRepository

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