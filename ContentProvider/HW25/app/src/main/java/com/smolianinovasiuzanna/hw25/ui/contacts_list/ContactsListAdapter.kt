package com.smolianinovasiuzanna.hw25.ui.contacts_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.hw25.data.Contact
import com.smolianinovasiuzanna.hw25.databinding.ItemContactBinding

class ContactsListAdapter(
    private val onItemClick: (contactId: Long) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsListViewHolder(
            binding,
            onItemClick
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as ContactsListViewHolder) {
            holder.bind(differ.currentList[position])
        }
    }

    fun setList(newContact: List<Contact>) {
        differ.submitList(newContact)
    }

    class ContactsListViewHolder(
        private val binding: ItemContactBinding,
        onItemClick: (contactId: Long) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        private var contactId: Long? = null

            init{
                binding.root.setOnClickListener {
                    contactId?.let { contactId -> onItemClick(contactId) }
                }
            }

        fun bind(contact: Contact) {
            with(binding){
                contactId = contact.id
                contactNameTextView.text = contact.name
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}