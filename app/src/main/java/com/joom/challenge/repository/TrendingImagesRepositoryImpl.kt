package com.joom.challenge.repository

import com.joom.challenge.model.ImagePublication

class TrendingImagesRepositoryImpl : TrendingImagesRepository, InMemoryRepository<ImagePublication>() {

    override fun findById(id: String): ImagePublication? {
        return items.firstOrNull {
            it.id == id
        }
    }
}