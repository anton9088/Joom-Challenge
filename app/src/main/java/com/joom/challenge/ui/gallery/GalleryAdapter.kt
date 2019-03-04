package com.joom.challenge.ui.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.joom.challenge.databinding.GalleryItemBinding
import com.joom.challenge.model.ImagePublication
import com.joom.challenge.pagination.PaginationAdapter
import com.joom.challenge.ui.NavigationController
import com.joom.challenge.util.inflater

class GalleryAdapter(
    private val spanCount: Int,
    private val navigationController: NavigationController
) : PaginationAdapter<ImagePublication, GalleryAdapter.ImageViewHolder>() {

    private var parentWidth: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)

        if (viewType == LOADING_VIEW_TYPE) {
            val layoutParams =
                viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams

            layoutParams.isFullSpan = true
        }

        return viewHolder
    }

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        if (parentWidth == 0) {
            parentWidth = parent.width - parent.paddingLeft - parent.paddingRight
        }

        val binding = GalleryItemBinding.inflate(parent.context.inflater(), parent, false)
        binding.navigationController = navigationController

        return ImageViewHolder(binding)
    }

    override fun bindItemViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePublication = items[position]
        val image = imagePublication.images.w480

        val aspectRatio =
            if (image.width != null && image.height != null && image.width > 0 && image.height > 0)
                image.width.toFloat() / image.height
            else 1f

        val width = parentWidth / spanCount
        val height = (width / aspectRatio).toInt()

        holder.itemView.layoutParams.width = width
        holder.itemView.layoutParams.height = height

        holder.bind(imagePublication)
    }

    class ImageViewHolder(private val binding: GalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imagePublication: ImagePublication) {
            binding.imageId = imagePublication.id
            binding.imageUrl = imagePublication.images.w480.url
            binding.executePendingBindings()
        }
    }
}