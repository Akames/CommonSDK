package com.akame.developkit.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ItemDecorationManager(
    val top: Int, val left: Int,
    val right: Int, val bottom: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                val spanCont = layoutManager.spanCount
                val position = parent.getChildAdapterPosition(view)
                if (position < spanCont) {
                    outRect.top = top
                }
                outRect.bottom = bottom

                val sp = right / spanCont
                val column = position % spanCont

                outRect.left = left
                when {
                    position % spanCont == spanCont - 1 -> {
                        outRect.right = right
                        outRect.left = outRect.left - sp * column
                    }

                    position % spanCont == 0 -> {
                        outRect.right = outRect.right + sp
                    }

                    else -> {
                        outRect.left = outRect.left - sp * column
                        outRect.right = outRect.right + sp + sp * column
                    }
                }
            }

            is StaggeredGridLayoutManager -> {
                val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                val count = layoutManager.spanCount
                val index = lp.spanIndex
                val pos = parent.getChildLayoutPosition(view)
                if (pos < count) {
                    outRect.top = top
                }
                outRect.bottom = bottom
                when {
                    index % count == 0 -> {
                        outRect.left = left
                        outRect.right = right / 2
                    }
                    index % count == count - 1 -> {
                        outRect.left = left / 2
                        outRect.right = right
                    }
                    else -> {
                        outRect.left = left / 2
                        outRect.right = right / 2
                    }
                }
            }

            is LinearLayoutManager -> {
                if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                    outRect.top = top
                    outRect.bottom = bottom
                    val pos = parent.getChildAdapterPosition(view)
                    if (pos == 0) {
                        outRect.left = left
                    } else {
                        outRect.left = left / 2
                    }
                    if (pos == state.itemCount - 1) {
                        outRect.right = right
                    } else {
                        outRect.right = right / 2
                    }
                } else {
                    outRect.left = left
                    outRect.right = right
                    val pos = parent.getChildAdapterPosition(view)
                    outRect.top = top
                    if (pos == state.itemCount - 1) {
                        outRect.bottom = bottom
                    }
                }
            }
        }
    }
}