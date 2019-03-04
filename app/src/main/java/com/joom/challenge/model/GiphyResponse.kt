package com.joom.challenge.model

interface GiphyResponse<T> {
    val data: T
    val meta: Meta
}