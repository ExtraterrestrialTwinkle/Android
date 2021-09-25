package com.smolianinovasiuzanna.hw21

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw21.databinding.ItemMovieBinding

class MovieListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> ()
{
    private val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as MovieHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setList(newMovies: List<ImdbResponse.Movie>) {
        differ.submitList(newMovies)
    }

    class MovieHolder (private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (movies: ImdbResponse.Movie) {
            bindMainInfo(movies.id, movies.posterLink, movies.title, movies.type, movies.year)
        }

        private fun bindMainInfo(id:String, posterLink: String, title: String, type: String, year: String) {
            with(binding){
                imdbIdTextView.text = id
                titleTextView.text = title
                typeTextView.text = type
                yearTextView.text = year

            Glide.with(itemView)
                .load(posterLink)
//                .error(R.drawable.ic_error)
//                .placeholder(R.drawable.ic_image)
                .into(posterImageView)
            }
        }
    }
    class DiffUtilCallback : DiffUtil.ItemCallback<ImdbResponse.Movie>() {
        override fun areItemsTheSame(oldItem: ImdbResponse.Movie, newItem: ImdbResponse.Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImdbResponse.Movie, newItem: ImdbResponse.Movie): Boolean {
            return oldItem == newItem
        }
    }

}