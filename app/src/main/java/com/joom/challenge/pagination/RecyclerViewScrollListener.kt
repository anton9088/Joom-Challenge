package com.joom.challenge.pagination

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

    private lateinit var tempArray: IntArray
    private var lastPos = -1

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // TODO добавить реализации для других layout manager
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager

        if (!::tempArray.isInitialized) {
            tempArray = IntArray(layoutManager.spanCount)
        }

        val lastVisibleItemPosition =
            layoutManager.findLastVisibleItemPositions(tempArray).max() ?: 0

        if (lastPos != lastVisibleItemPosition && layoutManager.itemCount > 0) {
            // вызываем onScrolled только если последний видимый элемент поменялся
            // т.е. не дергаем этот метод сто раз пока скроллим один и тот же элемент
            onScrolled(lastVisibleItemPosition, layoutManager.itemCount)

            lastPos = lastVisibleItemPosition
        }
    }

    abstract fun onScrolled(lastVisibleItemPosition: Int, totalItemCount: Int)
}