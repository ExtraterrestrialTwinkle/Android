package com.smolianinovasiuzanna.application.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.smolianinovasiuzanna.application.R
import com.smolianinovasiuzanna.application.Shows
import com.smolianinovasiuzanna.application.databinding.ItemFilmBinding



class FilmAdapterDelegate(
    private val onItemClick: (id: Long) -> Unit,
    private val onItemLongClick: (position: Int) -> Boolean
): AbsListItemAdapterDelegate <Shows.Film, Shows, FilmHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): FilmHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmHolder(
            binding,
            onItemClick,
            onItemLongClick
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
    onItemClick: (id: Long) -> Unit,
    onItemLongClick: (position: Int) -> Boolean
): BaseShowsHolder(binding, onItemClick, onItemLongClick) {

    fun bind(show: Shows.Film) {
        with(binding as ItemFilmBinding) {
            bindMainInfo(show.id)
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

