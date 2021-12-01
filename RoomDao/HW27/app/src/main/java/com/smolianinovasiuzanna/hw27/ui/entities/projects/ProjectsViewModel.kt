package com.smolianinovasiuzanna.hw27.ui.entities.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectsViewModel : ViewModel() {
    private val repository = ProjectRepository()

    private val projectsLiveData = MutableLiveData<List<Project>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val projects: LiveData<List<Project>>
        get() = projectsLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun loadList(){
        viewModelScope.launch(){
            try {
                projectsLiveData.postValue(repository.getAllProjects())
            } catch (t: Throwable){
                errorLiveData.postValue(t)
                projectsLiveData.postValue(emptyList())
            }
        }
    }

    fun deleteProject(project: Project){
        viewModelScope.launch {
            try{
                repository.deleteProject(projectId = project.id)
                loadList()
            } catch (t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }
}