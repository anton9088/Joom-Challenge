package com.joom.challenge.model

data class ImageResponse(
    override val data: ImagePublication,
    override val meta: Meta
) : GiphyResponse<ImagePublication>