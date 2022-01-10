package com.smolianinovasiuzanna.hw34.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw34.R
import com.smolianinovasiuzanna.hw34.data.movie_database.Movie
import com.smolianinovasiuzanna.hw34.databinding.ItemMovieBinding

class MovieAdapter: ListAdapter<Movie, RecyclerView.ViewHolder>(
    DiffCallback()
){
    fun loadList(movies: List<Movie>){
        submitList(movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).run {
            bind(currentList[position])
        }
    }

    class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie){
            binding.run {
                titleTextView.text = movie.title
                yearTextView.text = movie.year
                typeTextView.text = movie.type.name

                Glide.with(posterImageView)
                    .load(movie.posterLink)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)
                    .into(posterImageView)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return newItem == oldItem
        }
    }
}