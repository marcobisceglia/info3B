package com.example.divingapp2021

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.divingapp2021.databinding.AdapterBoatItemBinding
import com.example.mylibrary.MyRecyclerViewHolder
import com.example.mylibrary.MyRecyclerViewListAdapter

open class BoatAdapter(
        listener: View.OnClickListener? = null
) : MyRecyclerViewListAdapter<Boat>(DiffItemCallback(), listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.adapter_boat_item
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MyRecyclerViewHolder<out ViewBinding, Boat> {
        val view = this.buildItemView(parent, viewType)

        val binding = AdapterBoatItemBinding.bind(view).apply {
            this.root.setOnClickListener(listener)
        }
        return InfoItemViewHolder(binding)

    }

    // =============================================================================================

    class InfoItemViewHolder(binding: AdapterBoatItemBinding) :
            MyRecyclerViewHolder<AdapterBoatItemBinding, Boat>(binding) {

        override fun bind(data: Boat) {
            this.binding.nameTextView.text = data.id.toString()
            this.binding.modelTextView.text = data.model
            this.binding.seatsTextView.text = data.seats.toString()
            //


            this.binding.root.setTag(R.id.tag_item_item, data)
            this.binding.root.setTag(R.id.tag_item_position_absolute, this.adapterPosition)
            // this.binding.root.setTag(R.id.tag_item_position_owner, this.layoutPosition) TODO DA CAPIRE
        }
    }

    private class DiffItemCallback : DiffUtil.ItemCallback<Boat>() {
        override fun areItemsTheSame(oldItem: Boat, newItem: Boat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Boat, newItem: Boat): Boolean {
            return oldItem == newItem
        }
    }
}