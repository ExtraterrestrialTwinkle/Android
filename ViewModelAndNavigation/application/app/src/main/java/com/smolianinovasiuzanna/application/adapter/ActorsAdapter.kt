package com.smolianinovasiuzanna.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.application.Actors
import com.smolianinovasiuzanna.application.R
import com.smolianinovasiuzanna.application.databinding.ItemActorsBinding

class ActorsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val differ = AsyncListDiffer(this, ActorsDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemActorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorsHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as ActorsHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setImages(newActors: List<Actors>) {
        differ.submitList(newActors)
    }

    class ActorsHolder (private val binding: ItemActorsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (actors: Actors) {
            bindMainInfo(actors.name, actors.photoLink)
        }

        private fun bindMainInfo(name:String, imageUrl: String) {
           binding.actorsNameTextView.text = name
            Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_image)
                .into(binding.actorImageView)
        }
    }
    class ActorsDiffUtilCallback : DiffUtil.ItemCallback<Actors>() {
        override fun areItemsTheSame(oldItem: Actors, newItem: Actors): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actors, newItem: Actors): Boolean {
            return oldItem == newItem
        }
    }

}