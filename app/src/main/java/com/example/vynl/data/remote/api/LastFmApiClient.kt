package com.example.vynl.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LastFmApiClient {

    private const val BASE_URL =
        "https://ws.audioscrobbler.com/"

    val api: LastFmApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LastFmApiService::class.java)
    }
}
