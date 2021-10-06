package com.smolianinovasiuzanna.hw22_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw22_1.databinding.ItemMovieBinding

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
            bindMainInfo(
                movies.title,
                movies.year,
                movies.rating,
                movies.posterLink,
                movies.scores,
                movies.genre
            )
        }

        private fun bindMainInfo(
            title: String,
            year: Int?,
            rating: MovieRating = MovieRating.G,
            posterLink: String?,
            score: Map<String, String>,
            genre: String?
        ) {
            with(binding){
                titleTextView.text = """Title: $title"""
                yearTextView.text = """Year: ${year.toString()}"""
                ratingTextView.text = """Rating: ${rating.ageRating}"""
                scoreTextView.text = """Score: ${score}"""
                genreTextView.text = """Genre: ${genre.toString()}"""

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