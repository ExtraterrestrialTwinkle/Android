package com.skillbox.githubcoroutines.ui.current_user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.githubcoroutines.GithubResponse
import com.skillbox.githubcoroutines.R
import com.skillbox.githubcoroutines.databinding.ItemFollowingBinding

class CurrentUserAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> (){
    private val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as FollowingHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setList(newMovies: List<GithubResponse.Following>) {
        differ.submitList(newMovies)
    }

    class FollowingHolder (private val binding: ItemFollowingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (followings: GithubResponse.Following) {
            bindMainInfo(
                followings.avatarUrl,
                followings.login
            )
        }

        private fun bindMainInfo(
            followingAvatar: String,
            followingLogin: String
        ) {
            with(binding){

                followingLoginTextView.text = followingLogin

                Glide.with(itemView)
                    .load(followingAvatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)
                    .into(followingAvatarImageView)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<GithubResponse.Following>() {
        override fun areItemsTheSame(oldItem: GithubResponse.Following, newItem: GithubResponse.Following): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubResponse.Following, newItem: GithubResponse.Following): Boolean {
            return oldItem == newItem
        }
    }

}