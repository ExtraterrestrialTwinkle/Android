package com.smolianinovasiuzanna.hw33.features.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.features.chat.data.ChatRepository
import com.smolianinovasiuzanna.hw33.features.chat.data.User
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val scope = viewModelScope

    private val errorLiveData = MutableLiveData<Throwable>()
    private val messagesLiveData = MutableLiveData<List<MessageType.ChatMessage>>()
    private val newMessageLiveData = MutableLiveData<MessageType.ChatMessage>()

    val error: LiveData<Throwable>
        get() = errorLiveData
    val messages: LiveData<List<MessageType.ChatMessage>>
        get() = messagesLiveData
    val newMessage: LiveData<MessageType.ChatMessage>
        get() = newMessageLiveData


    fun receiveMessagesFromUser(userId: Long){
        scope.launch {
            try{
                messagesLiveData.postValue(repository.receiveAllMessagesFromUser(userId))
            } catch(t: Throwable){
                errorLiveData.postValue(t)
                messagesLiveData.postValue(emptyList())
            }
        }
    }

    fun sendMessage(message: MessageType.ChatMessage){
        scope.launch {
            try{
                repository.sendMessage(message)
                newMessageLiveData.postValue(message)
            } catch(t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }

    fun newUser(userId: Long, userName: String){
        scope.launch {
            try{
                val user = User(userId, userName)
                repository.newUser(user)
            } catch(t: Throwable){
                errorLiveData.postValue(t)
            }
        }
    }
}