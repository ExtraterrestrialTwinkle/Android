package com.smolianinovasiuzanna.hw27.ui.entities.contractors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.repository.ContractorRepository
import kotlinx.coroutines.launch

class ContractorsViewModel : ViewModel() {

    private val repository = ContractorRepository()
    private val contractorsLiveData = MutableLiveData<List<Contractor>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val contractors: LiveData<List<Contractor>>
        get() = contractorsLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData



    fun loadList(){
        viewModelScope.launch(){
            try {
                contractorsLiveData.postValue(repository.getAllContractors())
            } catch (t: Throwable){
                errorLiveData.postValue(t)
                contractorsLiveData.postValue(emptyList())
            }
        }
    }


    fun deleteContractor(contractor: Contractor) {
        viewModelScope.launch {
            try{
                repository.deleteContractor(contractorId = contractor.id)
                loadList()
            } catch (t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }


}