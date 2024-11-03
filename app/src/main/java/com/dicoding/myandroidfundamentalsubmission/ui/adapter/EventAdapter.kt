package com.dicoding.myandroidfundamentalsubmission.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.ListEventsItem
import com.dicoding.myandroidfundamentalsubmission.databinding.ItemEventsBinding

class EventAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    class MyViewHolder(val binding: ItemEventsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvOwner.text = "Organized by : ${event.ownerName}"
            Glide.with(binding.imgMediaCover.context)
                .load(event.mediaCover)
                .into(binding.imgMediaCover)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}