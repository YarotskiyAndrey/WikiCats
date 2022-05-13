package com.task.wikicats.model

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val wikiApiUrl = "https://en.wikipedia.org/w/"

    private fun retrofit() = Retrofit.Builder()
        .baseUrl(wikiApiUrl)
        .client(okHttpClient())
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .build()

    private fun okHttpClient() = OkHttpClient.Builder().build()

    val apiService: ApiService by lazy {
        retrofit().create(ApiService::class.java)
    }
}