package com.smolianinovasiuzanna.hw27.ui.adding.add_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.project.Project
import com.smolianinovasiuzanna.hw27.data.entities.project.ProjectStatus
import com.smolianinovasiuzanna.hw27.data.repository.ContractRepository
import com.smolianinovasiuzanna.hw27.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class AddProjectViewModel : ViewModel() {
    private val projectRepository = ProjectRepository()
    private val contractRepository = ContractRepository()

    private val errorLiveData = MutableLiveData<Throwable>()
    private val contractsLiveData = MutableLiveData<List<Contract>>()

    val error: LiveData<Throwable>
        get() = errorLiveData

    val contracts: LiveData<List<Contract>>
        get() = contractsLiveData

    fun getContracts(){
        try {
            viewModelScope.launch {
                val list = contractRepository.getAllContracts()
                contractsLiveData.postValue(list)
            }
        } catch(t: Throwable){
            errorLiveData.postValue(t)
            contractsLiveData.postValue(emptyList())
        }
    }

    fun save(
        title: String,
        duration: Int,
        status: ProjectStatus,
        contractId: Int
    ) {
        val project = Project(
            id = 0,
            title = title,
            duration = duration,
            status = status,
            contractId = contractId
        )

        viewModelScope.launch {
            try {
                projectRepository.saveProject(project)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}