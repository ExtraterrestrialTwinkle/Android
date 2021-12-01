package com.smolianinovasiuzanna.hw27.ui.entities.contracts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.repository.ContractRepository
import kotlinx.coroutines.launch

class ContractsViewModel : ViewModel() {

    private val repository = ContractRepository()

    private val errorLiveData = MutableLiveData<Throwable>()
    private val contractsLiveData = MutableLiveData<List<Contract>>()

    val contractsList: LiveData<List<Contract>>
        get() = contractsLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun loadList(contractorsId: Int){
        viewModelScope.launch(){
            try {
                contractsLiveData.postValue(repository.getContractsOfContractor(contractorsId))
            } catch (t: Throwable){
                errorLiveData.postValue(t)
                contractsLiveData.postValue(emptyList())
            }
        }
    }

    fun deleteContract(contract: Contract){
        viewModelScope.launch {
            try{
                repository.deleteContract(contractId = contract.id)
                loadList(contract.contractorId)
            } catch (t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }
}