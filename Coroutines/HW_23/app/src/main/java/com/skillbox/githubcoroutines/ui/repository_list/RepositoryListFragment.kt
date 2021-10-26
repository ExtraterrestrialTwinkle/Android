package com.skillbox.githubcoroutines.ui.repository_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.githubcoroutines.R
import com.skillbox.githubcoroutines.databinding.FragmentRepositoryListBinding

class RepositoryListFragment: Fragment(R.layout.fragment_repository_list) {

    private var _binding: FragmentRepositoryListBinding? = null
    private val binding: FragmentRepositoryListBinding get() = _binding!!
    private val viewModel: RepositoryListViewModel by viewModels()
    private var repositoryAdapter: RepositoryListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepositoryListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        binding.showStarredButton.setOnClickListener{
            findNavController().navigate(
                RepositoryListFragmentDirections
                    .actionRepositoryListFragmentToStarredRepositoriesFragment()
            )
        }
    }

    private fun initList(){
        repositoryAdapter = RepositoryListAdapter{ owner, repositoryName, description ->
            findNavController().navigate(
                RepositoryListFragmentDirections
                    .actionRepositoryListFragmentToDetailInfoFragment(owner, repositoryName, description))}
        with(binding.recyclerView) {
            adapter = repositoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun bindViewModel(){
        Log.d("bindViewModel", "start")
        viewModel.userRepository.observe(viewLifecycleOwner){newList ->
            repositoryAdapter?.setList(newList)
            Log.d("List", "$newList")
        }
        viewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            showLoader(isLoading)
        }
        viewModel.showRepositories()
    }

    private fun showLoader(isLoading: Boolean){
        when(isLoading){
            true -> binding.progress.isVisible = true
            false -> binding.progress.isGone = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        repositoryAdapter = null
    }
}