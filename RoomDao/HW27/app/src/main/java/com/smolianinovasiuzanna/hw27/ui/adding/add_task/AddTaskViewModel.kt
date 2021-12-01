package com.smolianinovasiuzanna.hw27.ui.adding.add_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.task.Task
import com.smolianinovasiuzanna.hw27.data.entities.task.TaskStatus
import com.smolianinovasiuzanna.hw27.data.repository.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val errorLiveData = MutableLiveData<Throwable>()
    private val saveSuccessLiveData = MutableLiveData<Unit>()

    val saveSuccess: LiveData<Unit>
        get() = saveSuccessLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun saveTask(
        title: String,
        description: String,
        duration: Int,
        status: TaskStatus,
        projectId : Int
    ){
        val task = Task(
            id = 0,
            title = title,
            description = description,
            duration = duration,
            status = status,
            projectId = projectId
        )
        try {
            viewModelScope.launch {
                saveSuccessLiveData.postValue(repository.saveTask(task))
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
        }
    }
}