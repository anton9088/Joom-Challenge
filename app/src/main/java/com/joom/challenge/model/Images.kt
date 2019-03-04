package com.joom.challenge.model

import com.squareup.moshi.Json

data class Images(
    @Json(name = "480w_still") val w480: Image
)