package com.smolianinovasiuzanna.hw21

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smolianinovasiuzanna.hw21.databinding.FragmentSearchMovieBinding


class SearchMovieFragment: Fragment(R.layout.fragment_search_movie) {
    private val binding by viewBinding (FragmentSearchMovieBinding::bind)
    private val viewModel: SearchMovieViewModel by viewModels()
    private var movieAdapter: MovieListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        bindViewModel()
        listeners()
    }

    private fun listeners(){
        with(binding){
            enterMovieTitleEditText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    title = s?.takeIf{it.isNotBlank()}.toString().replace(' ', '+')
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            enterMovieYearEditText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    year = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            val items = requireContext().resources.getStringArray(R.array.types)
            val adapter = ArrayAdapter(requireContext(), R.layout.list_types_item, items)
            (movieTypeExposedDropdownMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            autoComplete.setOnItemClickListener { _, _, position, _->
                when(position){
                    0 -> type = "film"
                    1 -> type = "series"
                    2 -> type = "episode"
                    3 -> type = ""
                }
            }
        }
        binding.searchButton.setOnClickListener {
            if (!isOnline()){
                showError("Ошибка сети")
            } else searchMovie()
        }
    }

    private fun initList() {
        movieAdapter = MovieListAdapter()
        with(binding.recyclerView) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun bindViewModel() {
        binding.repeatRequestButton.isGone = true
        binding.errorTextView.isGone = true
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showError(message)
            Log.d("Observe", "message = $message")
        }
        viewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
        viewModel.movieList.observe(viewLifecycleOwner) { newList -> movieAdapter?.setList(newList)
            Log.d("Observe", "NewList = $newList")}
    }

    private fun updateLoadingState(isLoading: Boolean) {
        with(binding){
            recyclerView.isVisible = isLoading.not()
            progress.isVisible = isLoading
            searchButton.isEnabled = isLoading.not()
            enterMovieYearEditText.isEnabled = isLoading.not()
            enterMovieTitleEditText.isEnabled = isLoading.not()
        }
    }

    private fun searchMovie(){
        Log.d("searchMovie", "$title, $year, $type")
        binding.repeatRequestButton.isGone = true
        binding.errorTextView.isGone = true
        viewModel.search(title, year, type)
    }

    private fun showError(message: String){
       Log.d("ShowError", message)
       with(binding){
           if (message.isNotBlank()){
               errorTextView.text = message
               errorTextView.isVisible = true
               repeatRequestButton.isVisible = true
               searchButton.isGone = true
               cancelRequestButton.isVisible = true
               repeatRequestButton.setOnClickListener {
                   searchMovie()
               }
               cancelRequestButton.setOnClickListener {
                   cancelRepeatedRequest()
               }
           }
       }
    }

    private fun cancelRepeatedRequest(){
        with(binding){
            errorTextView.isGone = true
            repeatRequestButton.isGone = true
            cancelRequestButton.isGone = true
            searchButton.isVisible = true
            enterMovieTitleEditText.text.clear()
            enterMovieYearEditText.text.clear()
        }
    }

    private fun isOnline(): Boolean {
        val connMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    companion object{
        var title: String = ""
        var year: String = ""
        var type: String = ""
    }

}