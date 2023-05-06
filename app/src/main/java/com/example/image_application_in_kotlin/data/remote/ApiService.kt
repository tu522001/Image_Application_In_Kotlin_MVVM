package com.example.image_application_in_kotlin.data.remote

import com.example.image_application_in_kotlin.data.model.DataFrames
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("frames_birthday.json")
    fun getData(): Call<DataFrames>
}