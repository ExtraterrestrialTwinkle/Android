package com.skillbox.github.ui.current_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.GithubResponse
import com.skillbox.github.data.CurrentUserRepository

class CurrentUserViewModel: ViewModel() {
    private val repository = CurrentUserRepository()
    private val userListLiveData = MutableLiveData<GithubResponse.User>()

    val userList: LiveData<GithubResponse.User>
        get() = userListLiveData

    fun showUserInfo() {
        repository.showUserInfo(
            onComplete = { user ->
                userListLiveData.postValue(user)
                Log.d("showUserInfo", "onComplete")
            },
            onError = { throwable ->
                userListLiveData.postValue(error("error"))
            }
        )
    }

}