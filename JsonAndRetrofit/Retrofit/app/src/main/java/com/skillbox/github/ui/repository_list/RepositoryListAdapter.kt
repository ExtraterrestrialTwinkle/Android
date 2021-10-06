package com.skillbox.github.ui.repository_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.github.GithubResponse
import com.skillbox.github.R
import com.skillbox.github.databinding.ItemRepositoryBinding

class RepositoryListAdapter(
    private val onItemClick: (owner: String, repositoryName: String, description: String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder> ()
{
    private val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserRepositoryHolder(
            onItemClick,
            binding
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as UserRepositoryHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setList(newMovies: List<GithubResponse.UserRepository>) {
        differ.submitList(newMovies)
    }

    class UserRepositoryHolder (
        onItemClick: (owner: String, repositoryName: String, description: String) -> Unit,
        private val binding: ItemRepositoryBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        private var reposOwner: String? = null
        private var reposName: String? = null
        private var reposDescription: String? = null

        init{
            binding.root.setOnClickListener{
                onItemClick(reposOwner?: "", reposName?: "", reposDescription?:"")
            }
        }

        fun bind (repositories: GithubResponse.UserRepository) {
            bindMainInfo(
                repositories.repositoryName.let{it.orEmpty()},
                repositories.repositoryUrl.let{it.orEmpty()},
                repositories.repositoryDescription.let{it.orEmpty()},
                repositories.owner.avatar.let{it.orEmpty()},
                repositories.owner.login.let{it.orEmpty()}
            )

        }

        private fun bindMainInfo(
            repositoryName: String,
            repositoryUrl: String,
            repositoryDescription: String,
            repositoryOwnersAvatarLink: String,
            repositoryOwnerName: String
        ) {
            with(binding){
                repositoryNameTextView.text = repositoryName
                repositoryUrlTextView.text = repositoryUrl
                repositoryDescriptionTextView.text = repositoryDescription

                reposOwner = repositoryOwnerName
                reposName = repositoryName
                reposDescription = repositoryDescription

                Glide.with(itemView)
                    .load(repositoryOwnersAvatarLink)
                    .circleCrop()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)
                    .into(avatarImageView)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<GithubResponse.UserRepository>() {
        override fun areItemsTheSame(oldItem: GithubResponse.UserRepository, newItem: GithubResponse.UserRepository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubResponse.UserRepository, newItem: GithubResponse.UserRepository): Boolean {
            return oldItem == newItem
        }
    }

}