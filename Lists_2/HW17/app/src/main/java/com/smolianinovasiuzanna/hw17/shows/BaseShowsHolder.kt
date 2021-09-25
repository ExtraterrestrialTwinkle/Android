package com.smolianinovasiuzanna.hw17.shows

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw17.R
import com.smolianinovasiuzanna.hw17.databinding.ItemPostersBinding


abstract class BaseShowsHolder(
    val binding: ViewBinding,
    onItemClick: (position: Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onItemClick(absoluteAdapterPosition)
        }
    }

}


