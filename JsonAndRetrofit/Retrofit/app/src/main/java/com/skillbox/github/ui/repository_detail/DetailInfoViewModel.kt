package com.skillbox.github.ui.repository_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.DetailInfoRepository

class DetailInfoViewModel(
    private val owner: String,
    private val repositoryName: String,
    private val description: String
): ViewModel() {

    private val repository = DetailInfoRepository(
        owner,
        repositoryName,
        isStarred = {starred -> isStarredLiveData.postValue(starred)},
        onError ={isStarredLiveData.postValue(false)}
    )
    private val isStarredLiveData = MutableLiveData<Boolean>()
    val isStarredRepos: LiveData<Boolean>
        get() = isStarredLiveData

    fun showIsStarred(){
        return repository.showInfo()
    }

    fun giveStar(){
        repository.giveStar()
    }

    fun deleteStar(){
        repository.deleteStar()
    }

}