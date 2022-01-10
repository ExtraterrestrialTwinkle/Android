package com.smolianinovasiuzanna.hw28.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw28.R
import com.smolianinovasiuzanna.hw28.data.Video
import com.smolianinovasiuzanna.hw28.databinding.ItemVideoBinding

class VideoListAdapter(
    private val onFavorite: (id: Long) -> Boolean,
    private val onTrash: (id: Long) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffUtilCallback())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoHolder(binding, onFavorite, onTrash)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as VideoHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setVideoList(newVideos: List<Video>) {
        differ.submitList(newVideos)
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].id
    }

    class VideoHolder(
        private val binding: ItemVideoBinding,
        onFavorite: (id: Long) -> Boolean,
        onTrash: (id: Long) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        private var currentItemId: Long = -1

        init{
            binding.favoriteButton.setOnClickListener {
                onFavorite(currentItemId)
                Log.d("Favorite button pressed", "id = $currentItemId")
            }
            binding.trashButton.setOnClickListener {
                onTrash(currentItemId)
            }
        }

        fun bind(video: Video) {
            bindMainInfo(video)
        }

        private fun bindMainInfo(
            video: Video
        ) {
            with(binding){
                currentItemId = video.id
                videoTitleTextView.text = video.title
                videoSizeTextView.text = String.format("%.2f",video.size.toDouble()/1_048_576.0) + "Mb"
                when(video.favorite){
                    true -> {
                        binding.favoriteButton.setImageResource(R.drawable.ic_favorite_24_gray)
                        Log.d("Favorite button", "isFavorite")
                    }
                    false -> {
                        binding.favoriteButton.setImageResource(R.drawable.ic_favorite_border_24_gray)
                        Log.d("Favorite button", "isFavorite.not()")
                    }
                }

                Glide.with(itemView)
                    .load(video.uri)
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_error_24)
                    .into(videoPreviewImageView)
            }
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return newItem == oldItem
        }
    }
}