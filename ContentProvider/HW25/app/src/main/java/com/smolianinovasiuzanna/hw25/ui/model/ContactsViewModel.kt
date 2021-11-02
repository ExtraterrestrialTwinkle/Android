package com.smolianinovasiuzanna.hw25.ui.model

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.smolianinovasiuzanna.hw25.data.ContactsRepository
import com.smolianinovasiuzanna.hw25.data.Contact
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

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
        scope.launch {
            var contacts = listOf<Contact>()
            launch {
                val contactsListAsync = async { repository.getAllContacts() }
                val contactNumbersAsync = async { repository.getPhoneNumbers() }
                val contactEmailAsync = async { repository.getEmails() }

                contacts = contactsListAsync.await()
                val contactNumbers = contactNumbersAsync.await()
                val contactEmails = contactEmailAsync.await()

                contacts.forEach { contact ->
                    contactNumbers[contact.id]?.let{ numbers ->
                        contact.phoneNumbers = numbers
                    }
                    contactEmails[contact.id]?.let { emails ->
                        contact.emails = emails
                    }
                }
            }.join()
            contactsLiveData.postValue(contacts)
        }
    }

    fun getContactInfo(contactId: Long){
        Log.d("ViewModel","getContactInfo")
        try {
            val contact = repository.getDetailContactInfo(contactId)
            detailInfoLiveData.postValue(contact)
        } catch(t: Throwable) {errorLiveData.postValue(t.message)}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteContact(contactId: Long){
        Log.d("viewModel","deleteContact")
        scope.launch(Dispatchers.IO + errorHandler){
            val contactsList = repository.deleteContact(contactId)
            contactsLiveData.postValue(contactsList)
        }
    }

    fun addContact(name: String, phones: ArrayList<String>, emails: ArrayList<String>){
        Log.d("ViewModel", "addContact")
        scope.launch(Dispatchers.IO + errorHandler){
            val newContacts = repository.addContact(name, phones, emails)
            contactsLiveData.postValue(newContacts)
        }
    }
}