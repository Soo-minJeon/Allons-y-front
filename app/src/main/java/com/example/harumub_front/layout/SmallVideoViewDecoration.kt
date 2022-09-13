package com.example.harumub_front.layout

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.harumub_front.layout.SmallVideoViewDecoration

class SmallVideoViewDecoration : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.adapter!!.itemCount
        val viewPosition = parent.getChildAdapterPosition(view)

        if (viewPosition == 0) {
            outRect.left = header
            outRect.right = divider / 2
        }
        else if (viewPosition == itemCount - 1) {
            outRect.left = divider / 2
            outRect.right = footer
        }
        else {
            outRect.left = divider / 2
            outRect.right = divider / 2
        }
    }

    companion object { // static final
        private const val divider = 12
        private const val header = 10
        private const val footer = 10
    }
}