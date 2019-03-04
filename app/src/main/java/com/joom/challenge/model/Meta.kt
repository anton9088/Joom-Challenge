package com.joom.challenge.model

import com.squareup.moshi.Json

data class Meta(
    val status: Int,
    @Json(name = "msg") val message: String
)