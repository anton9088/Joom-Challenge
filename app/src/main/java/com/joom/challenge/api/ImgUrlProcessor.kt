package com.joom.challenge.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ProcessImgUrl

/**
 * Заменяет:
 * 1) непрямые ссылки на картинку на прямые
 * 2) относительные ссылки на абсолютные
 */
class ImgUrlConverter {

    private val pattern = "https://media[0-9]*\\.giphy\\.com".toRegex()

    @FromJson
    @ProcessImgUrl
    fun processUrl(url: String): String {
        return if (url.startsWith("http")) {
            url.replace(pattern, "https://i.giphy.com")
        } else {
            "https://i.giphy.com/media/$url"
        }
    }

    @ToJson
    fun unused(@ProcessImgUrl url: String): String {
        return url
    }
}