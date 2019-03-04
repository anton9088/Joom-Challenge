package com.joom.challenge.ui.gallery

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Используется для задания отступов между элементами
 * в RecyclerView дополнительно нужно проставить отступы:
 * left = space
 * top = space / 2
 * bottom = space / 2
 */
class StaggeredSpacingItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    private val halfSpace = space / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        /*
            для StaggeredLayoutManager отступы должны быть фиксированными
            т.к. элементы могут перемещены для устранения пустых мест (gaps)
            для перемещенных элементов отступы не пересчитываются, а остаются старыми
         */

        outRect.left = 0
        outRect.top = halfSpace
        outRect.right = space
        outRect.bottom = halfSpace
    }
}