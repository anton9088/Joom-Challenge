package com.joom.challenge.api

import com.joom.challenge.model.GiphyResponse
import retrofit2.Response

fun <T: GiphyResponse<*>> checkAndGetResponse(response: Response<T>): T {
    val body = response.body()

    if (body != null) {
        val meta = body.meta

        if (meta.status != 200) {
            throw ApiException(meta.status, meta.message)
        }
    }

    if (!response.isSuccessful) {
        throw ApiException(response.code(), response.message())
    }

    checkNotNull(body) { "No body in http response" }

    return body
}