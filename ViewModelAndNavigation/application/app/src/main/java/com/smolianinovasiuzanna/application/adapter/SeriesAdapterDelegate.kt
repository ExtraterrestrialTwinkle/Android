package com.smolianinovasiuzanna.application.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.smolianinovasiuzanna.application.R
import com.smolianinovasiuzanna.application.Shows
import com.smolianinovasiuzanna.application.databinding.ItemSeriesBinding


class SeriesAdapterDelegate(
    private val onItemClick: (id: Long) -> Unit,
    private val onItemLongClick: (position: Int) -> Boolean
): AbsListItemAdapterDelegate<Shows.Series, Shows, SeriesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SeriesHolder {
        val binding = ItemSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesHolder(
            binding,
            onItemClick,
            onItemLongClick
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
    onItemClick: (id: Long) -> Unit,
    onItemLongClick: (position: Int) -> Boolean
): BaseShowsHolder(binding, onItemClick, onItemLongClick) {

    fun bind(show: Shows.Series) {
        with(binding as ItemSeriesBinding) {
            bindMainInfo(show.id)
            titleTextView.text = show.title
            creatorsTextView.text = "Creators: ${show.creators}"
            seasonsTextView.text = "Seasons: ${show.seasons}"

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