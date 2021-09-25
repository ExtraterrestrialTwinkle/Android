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
import com.smolianinovasiuzanna.hw17.databinding.ItemSeriesBinding
import inflate


class SeriesAdapterDelegate(
    private val onItemClick: (position: Int) -> Unit
): AbsListItemAdapterDelegate<Shows.Series, Shows, SeriesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SeriesHolder {
        val binding = ItemSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesHolder(
            binding,
            onItemClick
        )
    }

    override fun onBindViewHolder(
        item: Shows.Series,
        holder: SeriesHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    override fun isForViewType(item: Shows, items: MutableList<Shows>, position: Int): Boolean {
        return item is Shows.Series
    }
}
class SeriesHolder(
    binding: ItemSeriesBinding,
    onItemClick: (position: Int) -> Unit
): BaseShowsHolder(binding, onItemClick) {

    fun bind(show: Shows.Series) {
        with(binding as ItemSeriesBinding) {
            titleTextView.text = show.title
            creatorsTextView.text = "Director: ${show.creators}"
            seasonsTextView.text = "Year: ${show.seasons}"

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