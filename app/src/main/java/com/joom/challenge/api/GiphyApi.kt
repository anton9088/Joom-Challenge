package com.joom.challenge.api

import com.joom.challenge.model.ImageListResponse
import com.joom.challenge.model.ImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {

    @GET("v1/gifs/trending")
    fun trendingImages(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<ImageListResponse>

    @GET("v1/gifs/{id}")
    fun imageById(
        @Path("id") id: String
    ): Call<ImageResponse>
}