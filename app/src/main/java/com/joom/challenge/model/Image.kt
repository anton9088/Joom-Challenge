package com.joom.challenge.model

import com.joom.challenge.api.ProcessImgUrl

data class Image(
    @ProcessImgUrl val url: String,
    val width: Int?,
    val height: Int?
)