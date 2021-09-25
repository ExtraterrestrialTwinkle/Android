package com.smolianinovasiuzanna.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smolianinovasiuzanna.application.Actors
import com.smolianinovasiuzanna.application.ActorsFragment
import com.smolianinovasiuzanna.application.repository.ActorsRepository

class ActorsViewModel(id: Long) : ViewModel() {
    private val repository = ActorsRepository()

    private val actorsLiveData = MutableLiveData<List<Actors>>(
        repository.initActors(id)
    )

    val actors: LiveData<List<Actors>>
        get() = actorsLiveData
}