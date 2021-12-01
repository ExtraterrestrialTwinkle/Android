package com.smolianinovasiuzanna.hw27.ui.title

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.repository.FactoryRepository
import kotlinx.coroutines.launch

class TitleViewModel(application: Application): AndroidViewModel(application) {
    private val repository = FactoryRepository(application)
    private val errorLiveData = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = errorLiveData

    fun checkFirstTimeLaunch(context: Context){
        try {
            viewModelScope.launch {
                repository.checkFirstTimeLaunch(context)
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
        }

    }
}