package com.smolianinovasiuzanna.hw33.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.smolianinovasiuzanna.hw33.data.FirebaseToken.firebaseToken
import com.smolianinovasiuzanna.hw33.databinding.FragmentStartBinding
import timber.log.Timber
import timber.log.Timber.Forest.d
import java.lang.IllegalArgumentException

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding get() = _binding!!
    private val viewModel: StartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        receiveToken()
        buttonListener()
    }

    private fun bindViewModel(){
        viewModel.token.observe(viewLifecycleOwner) { token ->
            if(token == null) showError(IllegalArgumentException("Token was not received"))
            firebaseToken = token
            d("token = $token")
        }
        viewModel.error.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun showError(t: Throwable){
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
        Timber.e(t)
    }

    private fun receiveToken(){
        viewModel.receiveToken()
    }

    private fun buttonListener(){
        binding.navigationButton.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToSyncFragment())
        }
    }
}