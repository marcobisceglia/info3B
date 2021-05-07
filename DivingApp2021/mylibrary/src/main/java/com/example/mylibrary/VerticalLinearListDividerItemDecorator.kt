package com.example.mylibrary

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class VerticalLinearListDividerItemDecorator(
    context: Context,
    private val skipDividerCondition: SkipDividerCondition? = null
) : DividerItemDecoration(context, VERTICAL) {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //
        val skipDivider = skipDividerCondition?.shouldSkipDivider(view, parent) == true
        //
        if (skipDivider) {
            outRect.setEmpty()
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    interface SkipDividerCondition {
        fun shouldSkipDivider(
            view: View,
            parent: RecyclerView,
        ): Boolean
    }

    object LastItemSkipDividerCondition : SkipDividerCondition {
        override fun shouldSkipDivider(
            view: View,
            parent: RecyclerView,
        ): Boolean {
            val position = parent.getChildAdapterPosition(view)
            return position == (parent.adapter!!.itemCount - 1)
        }
    }
}