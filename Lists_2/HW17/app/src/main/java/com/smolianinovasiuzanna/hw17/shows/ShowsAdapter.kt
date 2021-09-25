package com.smolianinovasiuzanna.hw17.shows

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.smolianinovasiuzanna.hw17.Shows

class ShowsAdapter (
        private val onItemClick: (position: Int) -> Unit
    ): AsyncListDifferDelegationAdapter<Shows>(ShowsDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(FilmAdapterDelegate(onItemClick))
            .addDelegate(SeriesAdapterDelegate(onItemClick))
    }

    class ShowsDiffUtilCallback : DiffUtil.ItemCallback<Shows>() {
        override fun areItemsTheSame(oldItem: Shows, newItem: Shows): Boolean {
            return when {
                oldItem is Shows.Film && newItem is Shows.Film -> oldItem.id == newItem.id
                oldItem is Shows.Series && newItem is Shows.Series -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Shows, newItem: Shows): Boolean {
            return oldItem == newItem
        }

    }
}