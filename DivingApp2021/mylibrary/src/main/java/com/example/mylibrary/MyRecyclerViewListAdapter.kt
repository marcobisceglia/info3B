package com.example.mylibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class MyRecyclerViewListAdapter<D>(
    diffCallback: DiffUtil.ItemCallback<D>,
    protected val listener: View.OnClickListener? = null,
) : ListAdapter<D, MyRecyclerViewHolder<out ViewBinding, D>>(diffCallback) {
    val isEmpty: Boolean
        get() = this.itemCount == 0

    fun add(item: D) {
        val list = mutableListOf<D>().apply {
            addAll(currentList)
            add(item)
        }
        submitList(list)
    }

    protected fun buildItemView(parent: ViewGroup, @LayoutRes viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    fun clear() {
        this.submitList(null)
    }

    open fun contains(item: D): Boolean {
        return this.currentList.contains(item)
    }

    fun getItemIndex(item: D): Int {
        return this.currentList.indexOf(item)
    }

    override fun onBindViewHolder(
        holder: MyRecyclerViewHolder<out ViewBinding, D>,
        position: Int
    ) {
        holder.bind(this.getItem(position))
    }

    fun remove(item: D) {
        val list = mutableListOf<D>().apply {
            addAll(currentList)
            remove(item)
        }
        submitList(list)
    }
}