package com.smolianinovasiuzanna.hw16_8

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShowsAdapter (
        private val onItemClick: (position: Int) -> Unit
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var shows: List<Shows> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            Log.d("ShowsAdapter","onCreateViewHolder")
            return when(viewType) {
                TYPE_FILM -> FilmHolder(parent.inflate(R.layout.item_film), onItemClick)
                TYPE_SERIES -> SeriesHolder(parent.inflate(R.layout.item_series), onItemClick)
                else -> error("Incorrect viewType=$viewType")
            }
        }

        override fun getItemViewType(position: Int): Int {
            Log.d("ShowsAdapter","getItemViewType")
            return when(shows[position]) {
                is Shows.Series -> TYPE_SERIES
                is Shows.Film -> TYPE_FILM
            }
        }

        override fun getItemCount(): Int = shows.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("ShowsAdapter","onBindViewHolder")
            when(holder) {
                is FilmHolder -> {
                    val show = shows[position].let { it as? Shows.Film }
                        ?: error("Show at position = $position is not a film")
                    holder.bind(show)
                }

                is SeriesHolder -> {
                    val show = shows[position].let { it as? Shows.Series }
                        ?: error("Show at position = $position is not a series")
                    holder.bind(show)
                }

                else -> error("Incorrect view holder = $holder")
            }
        }

        fun updateShows(newShows: List<Shows>) {
            Log.d("SAdapter updateShows","$newShows")
            shows = newShows
        }

        abstract class BaseShowsHolder(
            view: View,
            onItemClick: (position: Int) -> Unit
        ): RecyclerView.ViewHolder(view) {

            private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            private val posterImageView: ImageView = view.findViewById(R.id.posterImageView)

            init {
                view.setOnClickListener {
                    onItemClick(absoluteAdapterPosition)
                }
            }

            protected fun bindMainInfo(
                title: String,
                posterLink: String
            ) {
                titleTextView.text = title

                if (posterLink.contains("content")) {
                    val uri: Uri = Uri.parse(posterLink)
                    posterImageView.setImageURI(null)
                    posterImageView.setImageURI(uri)

                }
                else if (posterLink.contains("http")){
                Glide.with(itemView)
                    .load(posterLink)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)
                    .into(posterImageView)
                }
            }

        }

        class FilmHolder(
            view: View,
            onItemClick: (position: Int) -> Unit
        ): BaseShowsHolder(view, onItemClick) {

            private val directorTextView: TextView = view.findViewById(R.id.directorTextView)
            private val yearTextView: TextView = view.findViewById(R.id.yearTextView)

            fun bind(show: Shows.Film) {
                bindMainInfo(show.title, show.posterLink)
                directorTextView.text = "Director: ${show.director}"
                yearTextView.text = "Year: ${show.year}"
            }
        }

        class SeriesHolder(
            view: View,
            onItemClick: (position: Int) -> Unit
        ): BaseShowsHolder(view, onItemClick) {

            private val creatorsTextView: TextView = view.findViewById(R.id.creatorsTextView)
            private val seasonsTextView: TextView = view.findViewById(R.id.seasonsTextView)

            fun bind(show: Shows.Series) {
                bindMainInfo(show.title, show.posterLink)
                creatorsTextView.text = "Creators: ${show.creators}"
                seasonsTextView.text = "Seasons: ${show.seasons}"
            }

        }

        companion object {
            private const val TYPE_FILM = 1
            private const val TYPE_SERIES = 2
        }
    }
