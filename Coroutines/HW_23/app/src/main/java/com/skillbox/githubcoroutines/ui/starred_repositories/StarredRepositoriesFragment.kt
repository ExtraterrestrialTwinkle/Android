package com.skillbox.githubcoroutines.ui.starred_repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.githubcoroutines.R
import com.skillbox.githubcoroutines.databinding.FragmentStarredRepositoriesBinding

class StarredRepositoriesFragment: Fragment(R.layout.fragment_starred_repositories) {

    private var _binding: FragmentStarredRepositoriesBinding? = null
    private val binding: FragmentStarredRepositoriesBinding get() = _binding!!
    private val viewModel: StarredRepositoriesViewModel by viewModels()
    private var starredAdapter: StarredRepositoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStarredRepositoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
    }

    private fun initList(){
        starredAdapter = StarredRepositoriesAdapter()
        with(binding.starredRecyclerView){
            adapter = starredAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

    }

    private fun bindViewModel(){
        viewModel.starredRepositoryList.observe(viewLifecycleOwner){repositories ->
            starredAdapter?.setList(repositories)
        }
        viewModel.showStarred()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        starredAdapter = null
    }
}