package com.smolianinovasiuzanna.hw33.features.chat.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.databinding.ItemChatIncomingBinding
import com.smolianinovasiuzanna.hw33.databinding.ItemChatOutgoingBinding
import timber.log.Timber

class ChatAdapter: ListAdapter<MessageType.ChatMessage, RecyclerView.ViewHolder>(
    DiffCallback()
) {
    fun addMessage(message: MessageType.ChatMessage){
        submitList(currentList + listOf(message))
        Timber.d("addMessage")
    }

    fun loadMessages(messages: List<MessageType.ChatMessage>){
        submitList(messages)
        Timber.d(messages.forEach{it.text}.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var binding: ViewBinding
        return when (viewType){
            INCOMING_MESSAGE -> {
                binding = ItemChatIncomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding).ItemChatIncomingViewHolder(binding)
            }
            else -> {
                binding = ItemChatOutgoingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding).ItemChatOutgoingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       with (holder as ItemViewHolder){
           holder.bind(currentList[position])
       }
    }

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position].userId == -1L){
            OUTGOING_MESSAGE
        } else {
            INCOMING_MESSAGE
        }   
    }

    open class ItemViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(message: MessageType.ChatMessage){}

        inner class ItemChatIncomingViewHolder(private val binding: ItemChatIncomingBinding): ItemViewHolder(binding){
            override fun bind(message: MessageType.ChatMessage) {
                Timber.d("Incoming message: $message")
                with(binding){
                    txtOtherUser.text = message.userName
                    txtOtherMessage.text = message.text
                    txtOtherMessageTime.text = message.createdAt
                }
            }
        }

        inner class ItemChatOutgoingViewHolder(private val binding: ItemChatOutgoingBinding): ItemViewHolder(binding){
            override fun bind(message: MessageType.ChatMessage) {
                Timber.d("Outgoing message: $message")
                with(binding) {
                    txtMyMessage.text = message.text
                    txtMyMessageTime.text = message.createdAt
                }
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<MessageType.ChatMessage>() {
        override fun areItemsTheSame(
            oldItem: MessageType.ChatMessage,
            newItem: MessageType.ChatMessage
        ): Boolean {
            return newItem.userId == oldItem.userId
        }

        override fun areContentsTheSame(
            oldItem: MessageType.ChatMessage,
            newItem: MessageType.ChatMessage
        ): Boolean {
            return newItem.createdAt == oldItem.createdAt && newItem == oldItem
        }
    }

    companion object{
        private const val INCOMING_MESSAGE = 1
        private const val OUTGOING_MESSAGE = 2
    }
}