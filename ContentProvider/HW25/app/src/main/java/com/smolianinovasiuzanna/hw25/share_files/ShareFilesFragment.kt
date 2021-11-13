package com.smolianinovasiuzanna.hw25.share_files

import android.R.attr
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.smolianinovasiuzanna.hw25.R
import com.smolianinovasiuzanna.hw25.databinding.FragmentShareFilesBinding
import java.io.File
import android.webkit.MimeTypeMap
import android.content.ContentResolver
import com.smolianinovasiuzanna.hw25.MyApplication
import java.util.*


class ShareFilesFragment : Fragment(R.layout.fragment_share_files) {

    private var _binding: FragmentShareFilesBinding? = null
    private val binding: FragmentShareFilesBinding get() = _binding!!
    private val viewModel: ShareFilesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareFilesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        with(binding){
            downloadAndShareButton.setOnClickListener {
                downloadAndShare()
            }
            url1TextView.setOnClickListener { copyPath(url1TextView.text.toString()) }
            url2TextView.setOnClickListener { copyPath(url2TextView.text.toString()) }
            url3TextView.setOnClickListener { copyPath(url3TextView.text.toString()) }
        }

    }

    private fun bindViewModel(){
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
            binding.fileUrlEditText.text.clear()
        }
        viewModel.fileUri.observe(viewLifecycleOwner){ uri ->
            if(uri != null){
                startIntent(uri)
            }  else {showError("Невозможно поделиться файлом")}
        }
    }

    private fun loader (isLoading: Boolean){
        Log.d("Fragment", "fun loader isLoading = $isLoading")
        with(binding) {
            when (isLoading) {
                true -> {
                    progress.isVisible = true
                    fileUrlEditText.isEnabled = false
                    downloadAndShareButton.isEnabled = false
                }
                false -> {
                    binding.progress.isGone = true
                    fileUrlEditText.isEnabled = true
                    downloadAndShareButton.isEnabled = true
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
            .setIcon(R.drawable.ic_error)
            .setMessage(message)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun downloadAndShare() {
        val url = binding.fileUrlEditText.text.toString()
        viewModel.downloadAndShareFile(url)
    }

    private fun copyPath(path: String){
        binding.fileUrlEditText.setText(path)
    }

    private fun startIntent(uri: Uri){
        requireContext().startActivity(viewModel.startIntent(uri))
    }

}