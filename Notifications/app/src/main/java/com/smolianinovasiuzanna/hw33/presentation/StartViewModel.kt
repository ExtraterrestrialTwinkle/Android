package com.smolianinovasiuzanna.hw33.presentation

import android.app.Application
import androidx.lifecycle.*
import com.smolianinovasiuzanna.hw33.core.MyApplication
import com.smolianinovasiuzanna.hw33.data.Repository
import kotlinx.coroutines.launch

class StartViewModel: ViewModel() {
    private val repository = Repository()

    private val tokenLiveData = MutableLiveData<String?>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val token: LiveData<String?>
        get() = tokenLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun receiveToken(){
        viewModelScope.launch {
            try{
                tokenLiveData.postValue(repository.receiveToken())
            } catch(t: Throwable){
                errorLiveData.postValue(t)
            }
        }

    }
}