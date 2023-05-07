package com.example.image_application_in_kotlin.data.repositories

import android.content.Context
import com.example.image_application_in_kotlin.data.model.DataFrames
import com.example.image_application_in_kotlin.data.model.PhotoFrames
import com.example.image_application_in_kotlin.data.remote.ApiService
import com.example.image_application_in_kotlin.data.remote.RetrofitClient
import retrofit2.Call

/**
 *  Create by TruongIT
 */

class PhotoRepository {
    private var apiService: ApiService? = null

    fun PhotoRepositorys() {
        apiService = RetrofitClient.getInstance().getApiService()
    }

    fun getListPhoto(): Call<DataFrames>? {
         if (apiService == null) {
             return null
        }
        return apiService?.getData()
    }

}