package com.joom.challenge.services

import com.joom.challenge.api.GiphyApi
import com.joom.challenge.api.checkAndGetResponse
import com.joom.challenge.model.ImagePublication
import com.joom.challenge.repository.TrendingImagesRepository
import javax.inject.Inject

class ImageService @Inject constructor(
    private val giphyApi: GiphyApi,
    private val trendingImagesRepository: TrendingImagesRepository
) {

    fun loadImageById(id: String): ImagePublication {
        return checkAndGetResponse(
            giphyApi.imageById(id).execute()
        ).data
    }

    fun getImageByIdFromRepository(id: String): ImagePublication? {
        return trendingImagesRepository.findById(id)
    }
}