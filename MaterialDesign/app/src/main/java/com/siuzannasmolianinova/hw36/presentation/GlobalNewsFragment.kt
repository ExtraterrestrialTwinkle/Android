package com.siuzannasmolianinova.hw36.presentation

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.siuzannasmolianinova.hw36.R
import com.siuzannasmolianinova.hw36.data.Country
import com.siuzannasmolianinova.hw36.data.db.Article
import com.siuzannasmolianinova.hw36.data.db.Category
import com.siuzannasmolianinova.hw36.databinding.FragmentGlobalNewsBinding
import com.siuzannasmolianinova.hw36.presentation.core.ConnectionState
import com.siuzannasmolianinova.hw36.presentation.core.ItemOffsetDecoration
import com.siuzannasmolianinova.hw36.presentation.core.State
import com.siuzannasmolianinova.hw36.presentation.view_model.GlobalNewsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber

class GlobalNewsFragment: Fragment() {
    private var _binding: FragmentGlobalNewsBinding? = null
    private val binding: FragmentGlobalNewsBinding get() = _binding!!
    private val viewModel: GlobalNewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var category: Flow<Category>
    private lateinit var country: Flow<Country>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlobalNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        category = flowOf(Category.GLOBAL)
        country = flowOf(Country.RUSSIA)
        viewModel.checkConnection()
        //viewModel.initLoading(category, country). apply { Timber.d("initLoading") }
        bindViewModel()
    }

    private fun initToolbar(){
        val toolbar: MaterialToolbar = binding.appBar.toolbar
        toolbar.title = "Global Newsfeed"
    }

    private fun initAdapter(){
        newsAdapter = NewsAdapter()
        val count = 2
        binding.newsFeed.run{
            adapter = newsAdapter
            layoutManager = GridLayoutManager(requireContext(), count)
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindViewModel(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { loadingState ->
                when (loadingState) {
                    is State.Loading -> showLoader(loadingState.isLoading)
                    is State.Success -> showList(loadingState.data)
                    is State.Error -> showError(loadingState.error)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.connectionState.collect { connectionState ->
                when (connectionState) {
                    ConnectionState.DISCONNECTED -> {
                        Timber.d("Collect flow: Disconnected")
                        viewModel.initLoading(category, country, false)
                        showSnackbar(resources.getString(R.string.disconnected), Snackbar.LENGTH_INDEFINITE,false)
                    }
                    ConnectionState.CONNECTED -> {
                        Timber.d("Collect flow: Connected")
                        viewModel.initLoading(category, country, true)
                        showSnackbar(resources.getString(R.string.refreshed), Snackbar.LENGTH_LONG, true)
                    }
                    ConnectionState.UNKNOWN ->
                        Timber.d("Collect flow: Unknown")
                }
            }
        }
    }

    private fun showLoader(state: Boolean) {
        binding.progress.progressContainer.isVisible = state
    }

    private suspend fun showList(data: Flow<List<Article>>) {
        data.collect {
            Timber.d(it.toString())
            newsAdapter.loadList(it)
        }
    }

    private fun showError(error: Throwable) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage(error.message)
            .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showSnackbar(message: String, length: Int, flag: Boolean){
        Timber.d("showSnackbar $message")
        Snackbar.make(binding.root, message, length)
            .setAnchorView(R.id.bottom_navigation)
            .apply{
                if(!flag){
                    setAction(R.string.retry){
                        viewModel.checkConnection()
                    }
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.cancelJob()
    }
}