package com.smolianinovasiuzanna.hw27.ui.adding.add_contractor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw27.data.entities.contractor.Contractor
import com.smolianinovasiuzanna.hw27.data.repository.ContractorRepository
import kotlinx.coroutines.launch

class AddViewModel : ViewModel() {
    private val repository = ContractorRepository()
    private val errorLiveData = MutableLiveData<Throwable>()

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun save(
        name: String
    ) {
        val contractor = Contractor(
            id = 0,
            name = name
        )
        viewModelScope.launch {
            try {
                repository.saveContractors(contractor)
            } catch (t: Throwable) {
                errorLiveData.postValue(t)
            }
        }
    }
}