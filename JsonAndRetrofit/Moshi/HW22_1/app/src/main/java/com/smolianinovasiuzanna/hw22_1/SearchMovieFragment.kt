package com.smolianinovasiuzanna.hw22_1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smolianinovasiuzanna.hw22_1.databinding.FragmentScoreDialogBinding
import com.smolianinovasiuzanna.hw22_1.databinding.FragmentSearchMovieBinding
import com.smolianinovasiuzanna.hw22_1.databinding.ItemMovieBinding
import com.squareup.moshi.Moshi


class SearchMovieFragment: Fragment(R.layout.fragment_search_movie) {
    private val binding by viewBinding (FragmentSearchMovieBinding::bind)
    private var _dialogBinding: FragmentScoreDialogBinding? = null
    private val dialogBinding: FragmentScoreDialogBinding get() = _dialogBinding!!
    private val viewModel: SearchMovieViewModel by viewModels()
    private var movieAdapter: MovieListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        bindViewModel()
        listeners()
    }


    private fun listeners(){
        with(binding) {
            enterMovieTitleEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    title = s?.takeIf { it.isNotBlank() }.toString().replace(' ', '+')
                }

                override fun afterTextChanged(s: Editable?) {}

            })

            binding.searchButton.setOnClickListener {
                if (!isOnline()) {
                    showError("Ошибка сети")
                } else searchMovie()
            }
            binding.scoreButton.setOnClickListener {
                getNewScoreToMovie()
            }

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
            enterMovieTitleEditText.isEnabled = isLoading.not()
        }
    }

    private fun searchMovie(){
        Log.d("searchMovie", title)
        binding.repeatRequestButton.isGone = true
        binding.errorTextView.isGone = true
        viewModel.search(title)
        binding.scoreButton.isVisible = true
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
               scoreButton.isGone = true
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
        }
    }

    private fun getNewScoreToMovie(){
        var source = ""
        var score = ""
        val scoreDialog = AlertDialog.Builder(requireContext())
        _dialogBinding = FragmentScoreDialogBinding.inflate(LayoutInflater.from(requireContext()))
        scoreDialog.setView(dialogBinding.root)
            .setPositiveButton("OK") { _, _ ->  viewModel.getNewScore(source, score)}
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(context, "You cancelled your score", Toast.LENGTH_SHORT)
                    .show()
            }

        dialogBinding.sourceInputEditText.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                source = s?.takeIf { it.isNotBlank() }.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dialogBinding.ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                dialogBinding.ratingBar.rating = rating
                Toast.makeText(
                    requireContext(), "рейтинг: $rating",
                    Toast.LENGTH_LONG
                ).show()
                score = rating.toString()
            }

        scoreDialog.create()
            .show()

    }

    private fun isOnline(): Boolean {
        val connMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun onDestroy() {
        super.onDestroy()
        _dialogBinding = null
    }

    companion object{
        var title: String = ""

    }

}