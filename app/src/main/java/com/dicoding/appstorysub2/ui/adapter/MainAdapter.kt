package com.dicoding.appstorysub2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.databinding.ItemStoryBinding

class MainAdapter(
    private val onItemClick: (StoryItemResponse, ImageView, TextView) -> Unit,
) : PagingDataAdapter<StoryItemEntity, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItemEntity?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story?.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = story?.name

                itemView.setOnClickListener {
                    val itemResponse = StoryItemResponse(
                        id = story?.id ?: "",
                        name = story?.name,
                        photoUrl = story?.photoUrl,
                        createdAt = story?.createdAt,
                        description = story?.description,
                        lat = story?.lat,
                        lon = story?.lon,
                    )
                    onItemClick(itemResponse, ivItemPhoto, tvItemName)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItemEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryItemEntity,
                newItem: StoryItemEntity,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryItemEntity,
                newItem: StoryItemEntity,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}