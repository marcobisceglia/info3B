package com.example.divingapp2021

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.divingapp2021.databinding.AdapterExcursionItemBinding
import com.example.mylibrary.MyRecyclerViewHolder
import com.example.mylibrary.MyRecyclerViewListAdapter

open class ExcursionAdapter(
    listener: View.OnClickListener? = null
) : MyRecyclerViewListAdapter<String>(DiffItemCallback(), listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.adapter_excursion_item
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRecyclerViewHolder<out ViewBinding, String> {
        val view = this.buildItemView(parent, viewType)

        val binding = AdapterExcursionItemBinding.bind(view).apply {
            this.root.setOnClickListener(listener)
        }
        return InfoItemViewHolder(binding)

    }

    // =============================================================================================

    class InfoItemViewHolder(binding: AdapterExcursionItemBinding) :
        MyRecyclerViewHolder<AdapterExcursionItemBinding, String>(binding) {

        override fun bind(data: String) {
            this.binding.timeTextView.text = data

            this.binding.root.setTag(R.id.tag_item_item, data)
            this.binding.root.setTag(R.id.tag_item_position_absolute, this.adapterPosition)
            // this.binding.root.setTag(R.id.tag_item_position_owner, this.layoutPosition)
        }
    }

    private class DiffItemCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}