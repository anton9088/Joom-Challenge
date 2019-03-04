package com.joom.challenge.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun giphyApi(): GiphyApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(API_KEY))
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(ImgUrlConverter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(GiphyApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://api.giphy.com"
        const val API_KEY = "9HQDBmMthFvBTapbqtV4go3CwSJJ7o1l"
        const val TIMEOUT = 15000L
    }
}