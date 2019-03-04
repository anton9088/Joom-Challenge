package com.joom.challenge.model

import com.squareup.moshi.Json

data class ImageListResponse(
    override val data: List<ImagePublication>,
    override val meta: Meta,
    @Json(name = "pagination") override val paginationInfo: PaginationInfo
) : PaginationResponse<ImagePublication>