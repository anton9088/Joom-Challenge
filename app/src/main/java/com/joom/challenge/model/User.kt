package com.joom.challenge.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "username") val name: String,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "profile_url") val profileUrl: String
)