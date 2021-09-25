package com.smolianinovasiuzanna.application.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.smolianinovasiuzanna.application.Shows


class ShowsAdapter (
   private val onItemClick: (id: Long) -> Unit,
   private val onItemLongClick: (position: Int) -> Boolean
): AsyncListDifferDelegationAdapter<Shows>(ShowsDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(FilmAdapterDelegate(onItemClick, onItemLongClick))
            .addDelegate(SeriesAdapterDelegate(onItemClick, onItemLongClick))
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