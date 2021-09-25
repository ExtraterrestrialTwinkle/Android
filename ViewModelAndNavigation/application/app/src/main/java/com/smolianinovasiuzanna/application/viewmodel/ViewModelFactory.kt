package com.smolianinovasiuzanna.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (
    private val id: Long
        ): ViewModelProvider.NewInstanceFactory(){
            override fun <T: ViewModel?> create(modelClass: Class<T>): T{
                return ActorsViewModel(id) as T
            }
        }