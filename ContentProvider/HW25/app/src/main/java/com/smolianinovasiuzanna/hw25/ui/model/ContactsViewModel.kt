package com.smolianinovasiuzanna.hw25.ui.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.smolianinovasiuzanna.hw25.data.ContactsRepository
import com.smolianinovasiuzanna.hw25.data.Contact
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ContactsRepository(application)
    private val contactsLiveData = MutableLiveData<List<Contact>>()
    private val errorLiveData = MutableLiveData<String>()
    private val detailInfoLiveData = MutableLiveData<Contact>()

    val contacts: LiveData<List<Contact>>
        get() = contactsLiveData

    val error: LiveData<String>
        get() = errorLiveData

    val contactInfo: LiveData<Contact>
        get() = detailInfoLiveData

    private val scope = viewModelScope
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("Coroutine exception", "${exception.message}")
        errorLiveData.postValue(exception.message)
    }

    fun loadContactsList(){
        Log.d("ContactsViewModel", "loadContactsList")
        scope.launch(Dispatchers.IO + errorHandler){
            var contactsList = listOf<Contact>()
            launch{
                contactsList = repository.getAllContacts()
            }.join()
            contactsLiveData.postValue(contactsList)
        }
    }

    fun getContactInfo(contactId: Long){
        Log.d("ViewModel","getContactInfo")
        try {
            val contact = repository.getDetailContactInfo(contactId)
            detailInfoLiveData.postValue(contact)
        }catch(t: Throwable) {errorLiveData.postValue(t.message)}
    }

    fun deleteContact(contactId: Long){
        Log.d("viewModel","deleteContact")
        scope.launch(Dispatchers.IO + errorHandler){
            contactsLiveData.postValue(repository.deleteContact(contactId))
        }
    }

    fun addContact(name: String, phones: List<String>, emails: List<String>){
        Log.d("ViewModel", "addContact")

        scope.launch(Dispatchers.IO + errorHandler){
            val contact = repository.addContact(name, phones, emails)
            contactsLiveData.postValue(contact)
        }
    }
}