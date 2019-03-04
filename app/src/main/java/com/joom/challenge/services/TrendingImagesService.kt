package com.joom.challenge.services

import com.joom.challenge.api.GiphyApi
import com.joom.challenge.api.checkAndGetResponse
import com.joom.challenge.model.ImageListResponse
import com.joom.challenge.model.ImagePublication
import com.joom.challenge.pagination.PaginationService
import com.joom.challenge.repository.TrendingImagesRepository
import javax.inject.Inject

class TrendingImagesService @Inject constructor(
    private val giphyApi: GiphyApi,
    repository: TrendingImagesRepository
) : PaginationService<ImagePublication>(repository) {

    override fun loadPaginationData(offset: Int, limit: Int): ImageListResponse {
        return checkAndGetResponse(
            giphyApi.trendingImages(offset, limit).execute()
        )
    }
}