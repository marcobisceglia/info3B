package com.example.mylibrary

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class MyRecyclerViewHolder<B : ViewBinding, D>(protected val binding: B) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(data: D)
    open fun bindPayload(payloads: List<Any?>){}

}