package com.joom.challenge.util

import android.content.Context
import com.joom.challenge.R
import com.joom.challenge.api.ApiException
import com.squareup.moshi.JsonDataException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorDescriptions @Inject constructor(
    private val context: Context
) {

    fun getErrorDescription(error: Throwable): String {
        return when (error) {
            is IOException -> context.getString(R.string.ioError)
            is ApiException -> context.getString(R.string.serverError, error.code)
            is JsonDataException -> context.getString(R.string.parseError)
            else -> context.getString(R.string.unknownError)
        }
    }

    fun getNoItemsText(): String {
        return context.getString(R.string.noItemsError)
    }
}