package com.joom.challenge.model

import com.squareup.moshi.Json

data class PaginationInfo(
    val offset: Int,
    val count: Int,
    @Json(name = "total_count") val totalCount: Int
)