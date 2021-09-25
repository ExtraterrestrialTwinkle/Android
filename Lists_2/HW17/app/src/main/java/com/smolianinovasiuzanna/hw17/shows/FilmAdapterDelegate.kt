package com.smolianinovasiuzanna.hw17.shows

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.smolianinovasiuzanna.hw17.R
import com.smolianinovasiuzanna.hw17.Shows
import com.smolianinovasiuzanna.hw17.databinding.ItemFilmBinding
import com.smolianinovasiuzanna.hw17.databinding.ItemPostersBinding
import com.smolianinovasiuzanna.hw17.posters.PostersAdapter
import inflate


class FilmAdapterDelegate(
    private val onItemClick: (position: Int) -> Unit
): AbsListItemAdapterDelegate <Shows.Film, Shows, FilmHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): FilmHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmHolder(
            binding,
            onItemClick
        )
    }

    override fun onBindViewHolder(
        item: Shows.Film,
        holder: FilmHolder,
        payloads: MutableList<Any>
    ) {
            holder.bind(item)
    }

    override fun isForViewType(item: Shows, items: MutableList<Shows>, position: Int): Boolean {
        return item is Shows.Film
    }
}
    class FilmHolder(
        binding: ItemFilmBinding,
        onItemClick: (position: Int) -> Unit
    ): BaseShowsHolder(binding, onItemClick) {

        fun bind(show: Shows.Film) {
            with(binding as ItemFilmBinding) {
                titleTextView.text = show.title
                directorTextView.text = "Director: ${show.director}"
                yearTextView.text = "Year: ${show.year}"

                if (show.posterLink.contains("content")) {
                    val uri: Uri = Uri.parse(show.posterLink)
                    posterImageView.setImageURI(null)
                    posterImageView.setImageURI(uri)

                } else if (show.posterLink.contains("http")) {
                    Glide.with(itemView)
                        .load(show.posterLink)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_error)
                        .into(posterImageView)
                }
            }
        }
    }

