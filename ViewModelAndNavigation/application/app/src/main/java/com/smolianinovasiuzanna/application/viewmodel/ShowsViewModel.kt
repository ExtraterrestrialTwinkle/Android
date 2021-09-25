package com.smolianinovasiuzanna.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smolianinovasiuzanna.application.Shows
import com.smolianinovasiuzanna.application.SingleLiveEvent
import com.smolianinovasiuzanna.application.Type
import com.smolianinovasiuzanna.application.repository.ShowsRepository

class ShowsViewModel() : ViewModel() {
    private val repository = ShowsRepository()

    private val showsLiveData = MutableLiveData<List<Shows>>(
        repository.initShows()
    )

    private val showToastLiveData = SingleLiveEvent<Unit>()

    val shows: LiveData<List<Shows>>
        get() = showsLiveData

    val showToast: LiveData<Unit>
        get() = showToastLiveData

    fun addShow(type: Type, args: List<String>) {
        val updatedList = repository.createShows(type, args, showsLiveData.value.orEmpty())
        showsLiveData.postValue(updatedList)
        showToastLiveData.postValue(Unit)

    }

    fun deleteShow(position: Int) {
        showsLiveData.postValue(
            repository.deleteShow(showsLiveData.value.orEmpty(), position)
        )
    }
}