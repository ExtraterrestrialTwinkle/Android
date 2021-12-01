package com.smolianinovasiuzanna.hw27.ui.adding.workspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.smolianinovasiuzanna.hw27.data.database.Database
import com.smolianinovasiuzanna.hw27.data.entities.workspace.Workspace
import com.smolianinovasiuzanna.hw27.data.repository.EmployeeRepository
import com.smolianinovasiuzanna.hw27.data.repository.WorkspaceRepository
import kotlinx.coroutines.launch

class WorkspaceViewModel: ViewModel() {
    private val workspaceRepository = WorkspaceRepository()

    private val errorLiveData = MutableLiveData<Throwable>()
    private val saveSuccessLiveData = MutableLiveData<Unit>()

    val error: LiveData<Throwable>
        get() = errorLiveData
    val saveSuccess: LiveData<Unit>
        get() = saveSuccessLiveData

    fun saveWorkspace(
        id: Int,
        equipment: Boolean,
        email: String?,
        passwordHash: String?
    ){

        val workspace = Workspace(
            id = id,
            equipment = equipment,
            email = email,
            passwordHash = passwordHash
        )
        try {
            viewModelScope.launch {
                Database.instance.withTransaction {
                    saveSuccessLiveData.postValue(workspaceRepository.saveWorkspace(workspace))
                }

            }
        }catch(t: Throwable){
            errorLiveData.postValue(t)
        }

    }
}