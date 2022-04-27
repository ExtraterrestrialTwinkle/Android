package com.smolianinovasiuzanna.hw33.features.chat.ui

import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw33.core.utils.DateTimeUtils
import com.smolianinovasiuzanna.hw33.data.MessageType
import com.smolianinovasiuzanna.hw33.databinding.FragmentChatBinding
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_LONG
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.Constants.BUNDLE_KEY_STRING
import com.smolianinovasiuzanna.hw33.features.chat.ui.utils.withArguments
import timber.log.Timber
import java.time.ZonedDateTime
import java.util.*

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = requireArguments().getLong(BUNDLE_KEY_LONG)
        val userName = requireArguments().getString(BUNDLE_KEY_STRING)!!
        Timber.d("userId = $userId, userName = $userName")
        bindViewModel()
        initAdapter()
        viewModel.newUser(userId, userName)
        viewModel.receiveMessagesFromUser(userId)
        buttonListener(userId)
    }

    private fun bindViewModel(){
        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            Timber.e(it)
        }
        viewModel.messages.observe(viewLifecycleOwner){
            chatAdapter.loadMessages(it)
            Timber.d("messages observer")
        }
        viewModel.newMessage.observe(viewLifecycleOwner){
            Timber.d(it.toString())
            chatAdapter.addMessage(it)
        }
    }

    private fun initAdapter(){
        Timber.d("initAdapter")
        chatAdapter = ChatAdapter()
        with(binding.chatArea) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonListener(userId: Long){
        binding.buttonSend.setOnClickListener {
            val messageText = binding.textMessage.text?.toString() ?: return@setOnClickListener
            val timestamp = DateTimeUtils.fromMillisToTimeString(ZonedDateTime.now().toInstant().toEpochMilli())
            val message = MessageType.ChatMessage(
                userId = -1,
                userName = "Me",
                createdAt = timestamp,
                text = messageText,
                addressee = userId
            )
            viewModel.sendMessage(message)
            binding.textMessage.text.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance(userId: Long, userName: String): ChatFragment{
            return ChatFragment().withArguments {
                putLong(BUNDLE_KEY_LONG, userId)
                putString(BUNDLE_KEY_STRING, userName)
            }
        }
    }
}