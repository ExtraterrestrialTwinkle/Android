package com.smolianinovasiuzanna.hw17.posters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw17.Posters
import com.smolianinovasiuzanna.hw17.R
import com.smolianinovasiuzanna.hw17.databinding.ItemPostersBinding
import inflate


class PostersAdapter (
    private val onItemClick: (position: Int) -> Unit
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {

        private val differ = AsyncListDiffer(this, PostersDiffUtilCallback())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = ItemPostersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostersHolder(
                binding,
                onItemClick
            )
        }

        override fun getItemCount(): Int = differ.currentList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with (holder as PostersHolder) {
                holder.bind(differ.currentList[position])
            }
        }

        fun setImages(newPosters: List<Posters>) {
            differ.submitList(newPosters)
        }

        class PostersHolder (
            private val binding: ItemPostersBinding,
            onItemClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

//            private val titlesTextView: TextView = view.findViewById(R.id.titlesTextView)
//            private val postersImageView: ImageView = view.findViewById(R.id.postersImageView)

            init {
                binding.root.setOnClickListener{
                    onItemClick(absoluteAdapterPosition)
                }
            }
            fun bind (poster: Posters) {
                bindMainInfo(poster.title, poster.posterLink)
            }

            fun bindMainInfo(title: String, imageUrl: String) {

                binding.titlesTextView.text = title
                Glide.with(itemView)
                    .load(imageUrl)
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.postersImageView)
            }
        }
        class PostersDiffUtilCallback : DiffUtil.ItemCallback<Posters>() {
            override fun areItemsTheSame(oldItem: Posters, newItem: Posters): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Posters, newItem: Posters): Boolean {
                return oldItem == newItem
            }
        }

    }





