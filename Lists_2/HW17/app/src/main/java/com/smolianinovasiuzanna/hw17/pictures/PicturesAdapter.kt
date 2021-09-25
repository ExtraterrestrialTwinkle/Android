package com.smolianinovasiuzanna.hw17.pictures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw17.Actors
import com.smolianinovasiuzanna.hw17.Pictures
import com.smolianinovasiuzanna.hw17.R
import com.smolianinovasiuzanna.hw17.databinding.ItemActorsBinding
import com.smolianinovasiuzanna.hw17.databinding.ItemPictureBinding

class PicturesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {

        private val differ = AsyncListDiffer(this, PicturesDiffUtilCallback())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PicturesHolder(binding)
        }

        override fun getItemCount(): Int = differ.currentList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with (holder as PicturesHolder) {
                holder.bind(differ.currentList[position])
            }
        }

        fun setImages(newPictures: List<Pictures>) {
            differ.submitList(newPictures)
        }



        class PicturesHolder (
            private val binding: ItemPictureBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind (pictures: Pictures) {
                bindMainInfo(pictures.pictureLink)
            }

            fun bindMainInfo(pictureLink: String) {

                Glide.with(itemView)
                    .load(pictureLink)
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.pictureImageView)
            }
        }
        class PicturesDiffUtilCallback : DiffUtil.ItemCallback<Pictures>() {
            override fun areItemsTheSame(oldItem: Pictures, newItem: Pictures): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pictures, newItem: Pictures): Boolean {
                return oldItem == newItem
            }
        }

    }
