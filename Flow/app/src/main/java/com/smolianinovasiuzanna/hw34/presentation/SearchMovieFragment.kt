package com.smolianinovasiuzanna.hw34.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.hw34.R
import com.smolianinovasiuzanna.hw34.data.movie_database.MovieType
import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import com.smolianinovasiuzanna.hw34.data.utils.checkedChangeFlow
import com.smolianinovasiuzanna.hw34.data.utils.textChangedFlow
import com.smolianinovasiuzanna.hw34.databinding.SearchMovieFragmentBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchMovieFragment : Fragment() {

    private var _binding: SearchMovieFragmentBinding? = null
    private val binding: SearchMovieFragmentBinding get() = _binding!!
    private val viewModel: SearchMovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchMovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initMovieListAdapter()
        bindViewModel()
        viewModel.bind(queryFlow(), movieTypeFlow())
    }

    private fun initToolbar() {
        val toolbar = binding.appBar.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow)
        toolbar.setNavigationOnClickListener {
            findNavController()
                .navigate(SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieListFragment())
        }
    }

    private fun initMovieListAdapter() {
        movieAdapter = MovieAdapter()
        binding.movieList.run {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { loadingState ->
                when (loadingState) {
                    is State.Loading -> showLoader(loadingState.isLoading)
                    is State.Success -> showList(loadingState.data)
                    is State.Error -> showError(loadingState.error)
                }
            }
        }
    }

    private fun queryFlow(): Flow<String> =
        binding.searchTextField.editText!!.textChangedFlow()

    private fun movieTypeFlow(): Flow<MovieType> = binding.radioGroup.checkedChangeFlow()
        .map {
            Timber.d(it.toString())
            when (it) {
                R.id.movieRadioButton -> MovieType.MOVIE
                R.id.seriesRadioButton -> MovieType.SERIES
                R.id.episodeRadioButton -> MovieType.EPISODE
                else -> error("Unexpected type")
            }
        }
        .catch { Timber.e(it) }


    private fun showLoader(state: Boolean) {
        binding.progressContainer.isVisible = state
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
        viewModel.cancelSearchingJob()
    }
}