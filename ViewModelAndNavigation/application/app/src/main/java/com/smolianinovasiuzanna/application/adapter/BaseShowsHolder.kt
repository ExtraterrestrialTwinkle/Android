package com.smolianinovasiuzanna.application.adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseShowsHolder(
    val binding: ViewBinding,
    onItemClick: (id: Long) -> Unit,
    onItemLongClick: (position: Int) -> Boolean
): RecyclerView.ViewHolder(binding.root) {

    private var itemId: Long? = null

    init {
        binding.root.setOnClickListener {
            onItemClick(itemId?: -1)
        }

        binding.root.setOnLongClickListener{
            onItemLongClick(bindingAdapterPosition)
        }
    }

    protected fun bindMainInfo (id: Long){
        itemId = id
    }
}

