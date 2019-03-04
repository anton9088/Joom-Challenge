package com.joom.challenge.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.joom.challenge.R
import com.joom.challenge.databinding.GalleryFragmentBinding
import com.joom.challenge.di.ViewModelFactory
import com.joom.challenge.model.ImagePublication
import com.joom.challenge.pagination.PaginationAdapter
import com.joom.challenge.pagination.PaginationFragment
import com.joom.challenge.pagination.PaginationViewModel
import com.joom.challenge.ui.views.LceStateLayout
import com.joom.challenge.ui.NavigationController
import kotlinx.android.synthetic.main.gallery_fragment.*
import javax.inject.Inject

class GalleryFragment : PaginationFragment<ImagePublication>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(GalleryViewModel::class.java)
    }

    override fun getViewModel(): PaginationViewModel<ImagePublication> {
        return viewModel
    }

    override fun createAdapter(): PaginationAdapter<ImagePublication, *> {
        return GalleryAdapter(SPAN_COUNT, navigationController)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return GalleryFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GalleryFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager =
                StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)

            // отключаем анимации StaggeredGridLayoutManager
            itemAnimator = null

            addItemDecoration(
                StaggeredSpacingItemDecoration(
                    resources.getDimensionPixelOffset(R.dimen.gallery_spacing)
                )
            )
        }
    }

    override fun getRecyclerView(): RecyclerView = recyclerView
    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipeRefreshLayout
    override fun getLceStateLayout(): LceStateLayout = lceLayout

    companion object {
        const val SPAN_COUNT = 2
    }
}