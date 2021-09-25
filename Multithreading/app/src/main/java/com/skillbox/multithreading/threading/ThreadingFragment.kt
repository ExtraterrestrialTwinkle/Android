package com.skillbox.multithreading.threading

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.multithreading.R
import com.skillbox.multithreading.databinding.FragmentThreadingBinding
import com.skillbox.multithreading.networking.Movie
import kotlinx.android.synthetic.main.fragment_threading.*
import kotlin.random.Random

class ThreadingFragment : Fragment(R.layout.fragment_threading) {

    private lateinit var movies: ArrayList<Movie>
    private var movieAdapter: MovieAdapter? = null
    private val binding by viewBinding(FragmentThreadingBinding::bind)
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var handler: Handler


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        val backgroundThread = HandlerThread("handler thread").apply {
            start()
        }
        handler = Handler(backgroundThread.looper)
        showList()
        observeViewModel()
        binding.swiperefresh.setOnRefreshListener {
            showList()
            binding.swiperefresh.isRefreshing = false
        }
    }
    private fun observeViewModel(){
        viewModel.movies.observe(viewLifecycleOwner) {newMovies ->
            movieAdapter?.setList(newMovies)
            handler.postDelayed({
                Toast.makeText(requireContext(), "Список обновлен", Toast.LENGTH_SHORT).show()
            }, 1000)
        }
    }
    private fun showList(){
        viewModel.requestMovies()
    }

    private fun initList() {
        movieAdapter = MovieAdapter()
        with(binding.recyclerView) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.looper.quit()
    }

}

