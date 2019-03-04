package com.joom.challenge.repository

import com.joom.challenge.model.ImagePublication

interface TrendingImagesRepository : Repository<ImagePublication> {
    fun findById(id: String): ImagePublication?
}