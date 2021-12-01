package com.smolianinovasiuzanna.hw27.ui.adding.add_contract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.contract.Contract
import com.smolianinovasiuzanna.hw27.data.entities.contract.ContractStatus
import com.smolianinovasiuzanna.hw27.data.repository.ContractRepository
import kotlinx.coroutines.launch

class AddContractViewModel : ViewModel() {
    private val repository = ContractRepository()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val existingContractLiveData = MutableLiveData<Contract>()
    private val saveSuccessLiveData = MutableLiveData<Unit>()

    val error: LiveData<Throwable>
        get() = errorLiveData

    val existingContract: LiveData<Contract>
        get() = existingContractLiveData

    val saveSuccess: LiveData<Unit>
        get() = saveSuccessLiveData

    fun init(id: Int) {
        viewModelScope.launch {
            try {
                val contract = repository.getContractById(id)
                if (contract.id != 0) existingContractLiveData.postValue(contract)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }

    fun save(
        id: Int,
        title: String,
        duration: Int,
        price: Float,
        status: ContractStatus,
        contractorId: Int
        ) {
        val contract = Contract(
            id = id,
            title = title,
            duration = duration,
            price = price,
            status = status,
            factoryId = 1,
            contractorId = contractorId
        )

        viewModelScope.launch {
            try {
                if (id == 0) {
                    repository.insertContract(contract)
                } else {
                    repository.updateContract(contract)
                }
                saveSuccessLiveData.postValue(Unit)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}