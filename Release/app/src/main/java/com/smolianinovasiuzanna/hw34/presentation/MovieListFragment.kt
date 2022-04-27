package com.smolianinovasiuzanna.hw34.presentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw34.R
import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import com.smolianinovasiuzanna.hw34.databinding.FragmentMovieListBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding: FragmentMovieListBinding get() = _binding!!
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initMovieListAdapter()
        bindViewModel()
        viewModel.loadList()
    }

    private fun initToolbar(){
        val toolbar = binding.appBar.toolbar
        toolbar.title = "Список фильмов"
    }

    private fun initMovieListAdapter() {
        movieAdapter = MovieAdapter()
        binding.allMoviesList.run {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { loadingState ->
                when (loadingState) {
                    is State.Success -> showList(loadingState.data)
                    is State.Error -> showError(loadingState.error)
                    is State.Loading -> return@collect
                }
            }
        }
    }

    private suspend fun showList(data: Flow<List<Movie>>) {
        data.collect {
            Timber.d(it.toString())
            movieAdapter.loadList(it)
        }
    }

    private fun showError(error: Throwable) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error!")
            .setMessage(error.message)
            .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.cancelMovieJob()
    }
}