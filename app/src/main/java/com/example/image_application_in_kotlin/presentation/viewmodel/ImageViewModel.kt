package com.example.image_application_in_kotlin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.image_application_in_kotlin.data.model.DataFrames
import com.example.image_application_in_kotlin.data.model.Define
import com.example.image_application_in_kotlin.data.model.Images
import com.example.image_application_in_kotlin.data.model.PhotoFrames
import com.example.image_application_in_kotlin.data.remote.AppResource
import com.example.image_application_in_kotlin.data.repositories.PhotoRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel : ViewModel(){
    private var photoRepository: PhotoRepository = PhotoRepository()
    private val _photoList = MutableLiveData<List<PhotoFrames>>()
    val photoList: LiveData<List<PhotoFrames>> = _photoList
    init {
        photoRepository.PhotoRepositorys()
    }


    fun getListPhoto() {
        photoRepository.getListPhoto()?.enqueue(object : Callback<DataFrames> {
            override fun onResponse(call: Call<DataFrames>, response: Response<DataFrames>) {
                // Xử lý dữ liệu khi nhận được response từ server
                if (response.isSuccessful) {
                     var photoFramesList = mutableListOf<PhotoFrames>()
                     var defineXList = mutableListOf<Define>()
                     var images = mutableListOf<Images>()
                    val photoFramesDTOList = response.body()?.listPhotoFrames ?: emptyList()
                    for (photoFrames in photoFramesDTOList) {
                        photoFramesList.add(
                            PhotoFrames(
                                photoFrames.cover,
                                photoFrames.defines,
                                photoFrames.folder,
                                photoFrames.icon,
                                photoFrames.lock,
                                photoFrames.name,
                                photoFrames.name_vi,
                                photoFrames.openPackageName,
                                photoFrames.totalImage
                            )
                        )

                        val defineXDTOList = photoFrames.defines ?: emptyList()

                        for (defineXDTO in defineXDTOList) {
                            defineXList.add(
                                Define(
                                    defineXDTO.end,
                                    defineXDTO.indexDefineCollage,
                                    defineXDTO.start,
                                    defineXDTO.totalCollageItemContainer
                                )
                            )
                            Log.d("AAA", "defineXList :" + defineXList.size)
                        }
                        Log.d("AAA", "photoFramesList :" + photoFramesList.size)
                        _photoList.postValue(photoFramesList)
                    }

                    photoFramesList.forEach { photoFrame ->
                        images.addAll(photoFrame.toImage())
                    }
                    images.forEach {
                        Log.d("checkImage", "onResponse: $it")
                    }
                    Log.d("fffff", "end image : " + photoFramesList.toString())


                }
            }

            override fun onFailure(call: Call<DataFrames>, t: Throwable) {
                // Xử lý lỗi khi request thất bại
                Log.d("AAA", "exception : ${t.message}")
            }
        })
    }
}