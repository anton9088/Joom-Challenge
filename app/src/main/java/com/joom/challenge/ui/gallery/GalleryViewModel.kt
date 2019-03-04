package com.joom.challenge.ui.gallery

import com.joom.challenge.model.ImagePublication
import com.joom.challenge.pagination.PaginationViewModel
import com.joom.challenge.services.TrendingImagesService
import com.joom.challenge.util.ErrorDescriptions
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    service: TrendingImagesService,
    errorDescriptions: ErrorDescriptions
) : PaginationViewModel<ImagePublication>(
    service,
    errorDescriptions
) {
    override fun getPageSize() = 30
    override fun getPaginationThreshold() = 15
}