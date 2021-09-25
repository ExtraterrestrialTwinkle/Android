package com.smolianinovasiuzanna.hw18

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smolianinovasiuzanna.hw18.databinding.ItemLocationBinding
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LocationInfoAdapter (
    private val onItemClick: (position: Int) -> Unit
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, LocationInfoHolder.LocationDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationInfoHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as LocationInfoHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setList(newLocation: List<LocationInfo>) {
        differ.submitList(newLocation)
    }

    class LocationInfoHolder(
        private val binding: ItemLocationBinding,
        onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(absoluteAdapterPosition)
            }
        }

            fun bind(location: LocationInfo) {
                val imageLink = location.imageLink
                val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                    .withZone(ZoneId.systemDefault())

                Glide.with(itemView)
                    .load(imageLink)
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.locationImageView)

                binding.locationDataTextView.text = location.info
                binding.timeTextView.text = formatter.format(location.timestamp)

            }

            class LocationDiffUtilCallback : DiffUtil.ItemCallback<LocationInfo>() {
                override fun areItemsTheSame(
                    oldItem: LocationInfo,
                    newItem: LocationInfo
                ): Boolean {
                    Log.d ("adapter", " old = $oldItem, new = $newItem")
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LocationInfo,
                    newItem: LocationInfo
                ): Boolean {
                    return oldItem == newItem
                }
            }

        }
 }
