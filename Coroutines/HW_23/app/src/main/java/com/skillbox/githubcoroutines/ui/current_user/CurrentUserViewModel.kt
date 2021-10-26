package com.skillbox.githubcoroutines.ui.current_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.data.CurrentUserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CurrentUserViewModel: ViewModel() {
    private val repository = CurrentUserRepository()
    private val userLiveData = MutableLiveData<GithubResponse.User>()
    private val followingListLiveData = MutableLiveData<List<GithubResponse.Following>>()

    val user: LiveData<GithubResponse.User>
        get() = userLiveData

    val followings: LiveData<List<GithubResponse.Following>>
        get() = followingListLiveData

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("CurrentUserViewModel", "Coroutine exception", throwable)
    }

    private val scope = viewModelScope

    fun showUserInfo() {
        scope.launch(errorHandler){
            val userInfo = async{
                repository.showUserInfo()
            }.await()
            val followings = async{
                repository.showFollowings()
            }.await()

            userLiveData.postValue(userInfo)
            followingListLiveData.postValue(followings)
        }
    }
}