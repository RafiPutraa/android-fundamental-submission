package com.dicoding.myandroidfundamentalsubmission.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myandroidfundamentalsubmission.data.remote.response.ListEventsItem
import com.dicoding.myandroidfundamentalsubmission.databinding.ItemCarouselBinding

class CarouselAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, CarouselAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }


    class MyViewHolder(private val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvEventOwner.text = "Organize by : ${event.ownerName}"
            Glide.with(binding.imgEvent.context).load(event.mediaCover).into(binding.imgEvent)
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