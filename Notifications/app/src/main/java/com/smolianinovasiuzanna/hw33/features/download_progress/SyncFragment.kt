package com.smolianinovasiuzanna.hw33.features.download_progress

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.smolianinovasiuzanna.hw33.core.ConnectivityBroadcastReceiver
import com.smolianinovasiuzanna.hw33.databinding.FragmentSyncBinding
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.max

class SyncFragment : Fragment() {

    private var _binding: FragmentSyncBinding? = null
    private val binding: FragmentSyncBinding get() = _binding!!
    private val viewModel: SyncViewModel by viewModels()
    private val receiver = ConnectivityBroadcastReceiver()
    private var maxProgress = 100L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonListener()
        bindViewModel()
    }

    private fun buttonListener(){
        binding.syncButton.setOnClickListener {
            requireContext().registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            synchronize()
        }
        binding.syncButtonWithoutDownloading.setOnClickListener {
            requireContext().registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            viewModel.synchronizeWithoutDownloading(maxProgress)
        }
    }

    private fun synchronize(){
        viewModel.synchronize()
    }

    private fun bindViewModel(){
        viewModel.preparing.observe(viewLifecycleOwner){
            if(it){
                lifecycleScope.launch {
                    ProgressNotifications(requireContext()).showInfiniteProgressNotification()
                    Timber.d("showInfiniteProgressNotification")
                }
            }
        }
        viewModel.download.observe(viewLifecycleOwner){
            if(!it) {
                lifecycleScope.launch {
                    ProgressNotifications(requireContext()).showFinalNotification()
                    Timber.d("showFinalNotification")
                }
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                ProgressNotifications(requireContext()).showErrorNotification()
                Timber.d("showErrorNotification")
            }
            Timber.e(it)
        }
        viewModel.maxProgress.observe(viewLifecycleOwner){
            if (it != null) {
                maxProgress = it
            }
        }
        viewModel.progress.observe(viewLifecycleOwner){ progress ->
            lifecycleScope.launch {
                progress?.toInt()?.let {
                    ProgressNotifications(requireContext())
                        .showDownloadProgressNotification(it, maxProgress.toInt())
                }
                Timber.d("showDownloadProgressNotification", progress, maxProgress)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}