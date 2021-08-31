package com.example.divingapp2021

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.divingapp2021.databinding.AdapterExcursionItemBinding
import com.example.mylibrary.MyRecyclerViewHolder
import com.example.mylibrary.MyRecyclerViewListAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class ExcursionAdapter(
    listener: View.OnClickListener? = null
) : MyRecyclerViewListAdapter<Trip>(DiffItemCallback(), listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.adapter_excursion_item
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRecyclerViewHolder<out ViewBinding, Trip> {
        val view = this.buildItemView(parent, viewType)

        val binding = AdapterExcursionItemBinding.bind(view).apply {
            this.root.setOnClickListener(listener)
        }
        return InfoItemViewHolder(binding)

    }

    // =============================================================================================

    class InfoItemViewHolder(binding: AdapterExcursionItemBinding) :
        MyRecyclerViewHolder<AdapterExcursionItemBinding, Trip>(binding) {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun bind(trip: Trip) {
            val format = DateTimeFormatter.ofPattern("HH:mm")

            this.binding.timeTextView.text =trip.dateTime?.format(format)

            this.binding.root.setTag(R.id.tag_item_item, trip)
            this.binding.root.setTag(R.id.tag_item_position_absolute, this.adapterPosition)
            // this.binding.root.setTag(R.id.tag_item_position_owner, this.layoutPosition)
        }
    }

    private class DiffItemCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.dateTimeString == newItem.dateTimeString

        }
    }
}