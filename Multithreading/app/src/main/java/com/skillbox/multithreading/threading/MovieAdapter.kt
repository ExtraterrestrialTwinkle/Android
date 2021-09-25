package com.skillbox.multithreading.threading

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.multithreading.databinding.ItemMovieBinding
import com.skillbox.multithreading.networking.Movie

class MovieAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, MovieDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as MovieHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setList(newMovies: List<Movie>) {
        differ.submitList(newMovies)
    }

    class MovieHolder ( private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind (movie: Movie) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                Log.d("Handler", "Initialization list task from thread = ${Thread.currentThread().name}")
                binding.titlesTextView.text = movie.title
                binding.yearTextView.text = movie.year.toString()
            }
            Log.d("views on thread", "${Thread.currentThread().name}")
        }
    }

    class MovieDiffUtilCallback: DiffUtil.ItemCallback<Movie>()  {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

}