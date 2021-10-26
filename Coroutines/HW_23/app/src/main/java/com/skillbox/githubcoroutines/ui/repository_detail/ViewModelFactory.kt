package com.skillbox.githubcoroutines.ui.repository_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (
    private val owner: String,
    private val repositoryName: String,
    private val description: String
): ViewModelProvider.NewInstanceFactory(){
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        return DetailInfoViewModel(owner, repositoryName, description) as T
    }
}