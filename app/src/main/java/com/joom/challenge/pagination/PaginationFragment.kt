package com.joom.challenge.pagination

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.joom.challenge.R
import com.joom.challenge.pagination.PaginationAdapter.LoadMoreState
import com.joom.challenge.ui.views.LceStateLayout
import com.joom.challenge.ui.NetworkRequestState
import com.joom.challenge.ui.NetworkRequestState.Type.*
import com.joom.challenge.util.ErrorDescriptions
import com.joom.challenge.util.autoCleared
import com.joom.challenge.util.observe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class PaginationFragment<T : PaginationItem> : DaggerFragment() {

    private var paginationAdapter by autoCleared<PaginationAdapter<T, *>>()
    private var isLayoutManagerStateRestored = false

    @Inject
    lateinit var errorDescriptions: ErrorDescriptions

    abstract fun createAdapter(): PaginationAdapter<T, *>

    abstract fun getViewModel(): PaginationViewModel<T>

    abstract fun getLceStateLayout(): LceStateLayout

    abstract fun getSwipeRefreshLayout(): SwipeRefreshLayout

    abstract fun getRecyclerView(): RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paginationAdapter = createAdapter().apply {
            loadMoreHandler = object : PaginationAdapter.LoadMoreHandler {
                override fun onLoadMoreClicked(view: View) {
                    getViewModel().onLoadMoreClicked()
                }
            }
        }

        val scrollListener = object : RecyclerViewScrollListener() {
            override fun onScrolled(lastVisibleItemPosition: Int, totalItemCount: Int) {
                getViewModel().onListScrolled(lastVisibleItemPosition, totalItemCount)
            }
        }

        getRecyclerView().apply {
            adapter = paginationAdapter
            addOnScrollListener(scrollListener)
        }

        getSwipeRefreshLayout().apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener { getViewModel().onRefresh() }
        }

        getLceStateLayout().onRetryClickedListener = {
            getViewModel().onRetryClicked()
        }

        observe(getViewModel().getLoadInitialState()) { state ->
            when (state.type) {
                LOADING -> {
                    getLceStateLayout().showLoading()
                }
                COMPLETE -> {
                    // do nothing
                }
                ERROR -> {
                    getLceStateLayout().showError(state.errorText!!)
                }
            }
        }

        observe(getViewModel().getLoadMoreState()) { state ->
            when (state.type) {
                LOADING -> {
                    paginationAdapter.loadMoreState = LoadMoreState.PROGRESS
                }
                COMPLETE -> {
                    paginationAdapter.loadMoreState = LoadMoreState.BUTTON

                    if (state.isDataOutdated && !state.isDataOutdatedHandled) {
                        getRecyclerView().scrollToPosition(0)
                        state.isDataOutdatedHandled = true
                    }
                }
                ERROR -> {
                    paginationAdapter.loadMoreState = LoadMoreState.BUTTON
                    showToastFromErrorState(state)
                }
            }
        }

        observe(getViewModel().getRefreshState()) { state ->
            when (state.type) {
                LOADING -> {
                    getSwipeRefreshLayout().isRefreshing = true
                }
                COMPLETE -> {
                    getSwipeRefreshLayout().isRefreshing = false
                }
                ERROR -> {
                    getSwipeRefreshLayout().isRefreshing = false
                    showToastFromErrorState(state)
                }
            }
        }

        observe(getViewModel().getData()) {
            paginationAdapter.items = it
        
            if (it.isNotEmpty()) {
                restoreLayoutManagerPosition(savedInstanceState)
                getLceStateLayout().showContent()
            } else {
                getLceStateLayout().showError(errorDescriptions.getNoItemsText())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(
            LAYOUT_MANAGER_STATE,
            getRecyclerView().layoutManager!!.onSaveInstanceState()
        )
    }

    private fun restoreLayoutManagerPosition(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && !isLayoutManagerStateRestored) {
            val layoutManagerSavedState =
                savedInstanceState.getParcelable<Parcelable>(LAYOUT_MANAGER_STATE)

            getRecyclerView().layoutManager!!.onRestoreInstanceState(layoutManagerSavedState)

            isLayoutManagerStateRestored = true
        }
    }

    private fun showToastFromErrorState(state: NetworkRequestState) {
        if (!state.isErrorShown) {
            Toast.makeText(context, state.errorText, Toast.LENGTH_SHORT)
                .show()

            state.isErrorShown = true
        }
    }

    companion object {
        const val LAYOUT_MANAGER_STATE = "layoutManagerState"
    }
}