package com.joom.challenge.pagination

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joom.challenge.databinding.LoadingItemBinding
import com.joom.challenge.util.inflater

abstract class PaginationAdapter<M, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = listOf<M>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var loadMoreState = LoadMoreState.BUTTON
        set(value) {
            if (field != value) {
                field = value
                notifyItemChanged(itemCount - 1)
            }
        }

    var loadMoreHandler: LoadMoreHandler? = null

    abstract fun createItemViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun bindItemViewHolder(holder: VH, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_VIEW_TYPE -> {
                val binding = LoadingItemBinding.inflate(parent.context.inflater(), parent, false)
                binding.loadMoreHandler = loadMoreHandler
                LoadMoreViewHolder(binding)
            }
            else -> createItemViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        @Suppress("UNCHECKED_CAST")
        when (getItemViewType(position)) {
            LOADING_VIEW_TYPE -> {
                val loadingViewHolder = holder as LoadMoreViewHolder
                loadingViewHolder.setLoadMoreState(loadMoreState)
            }
            else -> bindItemViewHolder(holder as VH, position)
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 0
        else items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingItem(position)) {
            LOADING_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
    }

    private fun isLoadingItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    enum class LoadMoreState {
        PROGRESS, BUTTON
    }

    interface LoadMoreHandler {
        fun onLoadMoreClicked(view: View)
    }

    class LoadMoreViewHolder(private val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val handler = Handler()

        fun setLoadMoreState(loadMoreState: LoadMoreState) {
            if (loadMoreState == LoadMoreState.BUTTON) {
                // avoid blinking
                handler.postDelayed({
                    binding.loadMoreState = loadMoreState
                    binding.executePendingBindings()
                }, 300)
            } else {
                handler.removeCallbacksAndMessages(null)

                binding.loadMoreState = loadMoreState
                binding.executePendingBindings()
            }
        }
    }

    companion object {
        const val LOADING_VIEW_TYPE = -1
        const val ITEM_VIEW_TYPE = 0
    }
}