package com.siuzannasmolianinova.hw35.presentation

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.siuzannasmolianinova.hw35.R
import com.siuzannasmolianinova.hw35.core.Constants.DOWNLOAD_WORK_ID
import com.siuzannasmolianinova.hw35.databinding.FragmentDownloadBinding
import org.intellij.lang.annotations.RegExp
import timber.log.Timber

class DownloadFragment : Fragment() {
    private var _binding: FragmentDownloadBinding? = null
    private val binding: FragmentDownloadBinding get() = _binding!!
    private val viewModel: DownloadViewModel by viewModels()
    private lateinit var url: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWorkManager()
        showEnqueueText(false) // чтобы не отображать текст при перезапуске приложения
        binding.cancelButton.setOnClickListener {
            Timber.d("Downloading cancelled")
            cancelDownloading()
        }
        binding.searchButton.setOnClickListener{
            Timber.d("Downloading started")
            downloadFile()
        }
    }

    private fun observeWorkManager(){
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DOWNLOAD_WORK_ID) // наблюдаем изменение сосотояния
            .observe(viewLifecycleOwner) {
                if(it.isNotEmpty()){
                    Timber.d("observe WorkInfo, ${it.first().state}")
                    handleWorkInfo(it.first())
                }
            }
    }

    private fun handleWorkInfo(workInfo: WorkInfo){
        when (workInfo.state){
            WorkInfo.State.SUCCEEDED -> {
                Timber.d(workInfo.state.toString())
                showProgress(false)
                blockUi(false)
                showToast("Download is finished")
            }
            WorkInfo.State.FAILED -> {
                Timber.d(workInfo.state.toString())
                showProgress(false)
                showEnqueueText(false)
                blockUi(false)
                showError()
            }
            WorkInfo.State.ENQUEUED -> {
                Timber.d(workInfo.state.toString())
                showProgress(true)
                blockUi(true)
                showEnqueueText(true)
            }
            WorkInfo.State.RUNNING -> {
                Timber.d(workInfo.state.toString())
                showEnqueueText(false)
                blockUi(true)
                showProgress(true)
            }
            WorkInfo.State.CANCELLED -> {
                Timber.d(workInfo.state.toString())
                showToast("Загрузка отменена")
                blockUi(false)
                showProgress(false)
            }
            else -> Timber.d(workInfo.state.toString())
        }
    }

    private fun downloadFile(){
        url = binding.searchTextField.editText?.text.toString()
        if(url.isEmpty().not()) viewModel.downloadFile(url)
    }

    private fun cancelDownloading(){
        viewModel.stopDownloading()
    }

    private fun showToast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        Timber.d(text)
    }

    private fun showError(){
        AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setIcon(R.drawable.ic_error)
            .setMessage("Загрузка прервана. Возобновить загрузку?")
            .setNeutralButton("Retry"){ _, _ ->
                retryDownloading(url)
            }
            .show()
    }

    private fun retryDownloading(url: String){
        viewModel.downloadFile(url)
    }

    private fun showEnqueueText(isEnqueued: Boolean){
        Timber.d("isEnqueued = $isEnqueued")
        binding.awaitDownloadingTextView.isVisible = isEnqueued
    }

    private fun showProgress(isDownloading: Boolean){
        Timber.d("isDownloading = $isDownloading")
        binding.progress.isVisible = isDownloading
    }

    private fun blockUi(isDownloading: Boolean){
        Timber.d("BlockUI = ${!isDownloading}")
        binding.searchTextField.editText?.isEnabled = !isDownloading
        binding.searchButton.isVisible = !isDownloading
        binding.cancelButton.isVisible = isDownloading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}