package com.smolianinovasiuzanna.hw24.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.smolianinovasiuzanna.hw24.R
import com.smolianinovasiuzanna.hw24.databinding.FragmentDownloadFileBinding
import kotlin.properties.Delegates

class DownloadFileFragment: Fragment(R.layout.fragment_download_file) {

    private val viewModel: DownloadFileViewModel by viewModels()
    private var _binding: FragmentDownloadFileBinding? = null
    private val binding: FragmentDownloadFileBinding get() = _binding!!
    private var url: String by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadFileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkFirstLaunch(requireContext())
        bindViewModel()

        with(binding){

            button.setOnClickListener{
                Log.d("Fragment", "button clicked")
                url = editText.text.toString()
                Log.d("url", url)
                viewModel.checkUrlName(url)
            }
        }
    }

    private fun bindViewModel(){
        Log.d("Fragment", "bind view model")
        viewModel.download.observe(viewLifecycleOwner){
            Log.d("Fragment", "viewModel.download.observe")
        }
        viewModel.loading.observe(viewLifecycleOwner){ isLoading ->
            Log.d("Fragment", "viewModel.loading.observe")
            loader(isLoading)
        }
        viewModel.error.observe(viewLifecycleOwner){ exceptionMessage ->
            Log.d("Fragment", "viewModel.error.observe")
            showError(exceptionMessage)
            binding.editText.text.clear()
        }
        viewModel.checkFile.observe(viewLifecycleOwner){ isExist ->
            when(isExist){
                true ->  Toast.makeText(requireContext(), R.string.file_exists, Toast.LENGTH_SHORT).show()
                false ->  viewModel.downloadFile(url)
            }
        }
    }

    private fun loader (isLoading: Boolean){
        Log.d("Fragment", "fun loader isLoading = $isLoading")
        with(binding) {
            when (isLoading) {
                true -> {
                    progress.isVisible = true
                    editText.isEnabled = false
                    button.isEnabled = false
                }
                false -> {
                    binding.progress.isGone = true
                    editText.isEnabled = true
                    button.isEnabled = true
                    Toast.makeText(requireContext(), R.string.download_is_successful, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showError(message: String){
        Log.d("Fragment", "fun showError message = $message")
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setMessage(message)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}