package com.example.image_application_in_kotlin.data.remote

import com.example.image_application_in_kotlin.common.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient private constructor() {

    private var retrofit: Retrofit? = null
    private var apiService: ApiService? = null

    init {
        retrofit = createRetrofit()
        apiService = retrofit!!.create(ApiService::class.java)
    }

    companion object {
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }
            return instance as RetrofitClient
        }
    }

    private fun createRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun getApiService(): ApiService? {
        return if (apiService != null) {
            apiService
        } else {
            retrofit!!.create(ApiService::class.java)
        }
    }
}