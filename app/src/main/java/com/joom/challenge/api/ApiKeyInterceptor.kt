package com.joom.challenge.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val httpUrl = request.url().newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val requestWithApiKey = request.newBuilder()
            .url(httpUrl)
            .build()

        return chain.proceed(requestWithApiKey)
    }
}