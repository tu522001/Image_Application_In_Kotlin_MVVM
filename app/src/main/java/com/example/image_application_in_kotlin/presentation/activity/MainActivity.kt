package com.example.image_application_in_kotlin.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.image_application_in_kotlin.data.model.Images
import com.example.image_application_in_kotlin.databinding.ActivityMainBinding
import com.example.image_application_in_kotlin.presentation.adapter.ImageAdapter
import com.example.image_application_in_kotlin.presentation.viewmodel.ImageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var images_list : MutableList<Images>
    private lateinit var layoutManager: LinearLayoutManager

    var imagesViewModel: ImageViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Sử dụng biến binding để truy cập các thành phần trong layout
        setContentView(binding.root)

        images_list = mutableListOf<Images>()
        initial()


//        imageAdapter.addListProduct(images_list)
        // Gọi phương thức getListPhoto() của ImageViewModel


        // Khởi tạo đối tượng ImageViewModel
        // Trong hàm onCreate():
//        imagesViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
//        imagesViewModel!!.getListPhoto()
//        imagesViewModel!!.photoList.observe(this, { photoList ->
//            val imagesList = photoList.flatMap {
//                    photoFrame -> photoFrame.toImages() }
//            imageAdapter.addListPhoto(imagesList)
//        })


        // Khởi tạo đối tượng ImageViewModel
        imagesViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        imagesViewModel!!.getListPhoto()
        imagesViewModel!!.photoList.observe(this) { photoList ->
            val imagesList = mutableListOf<Images>()
            for (photoFrame in photoList) {
                imagesList.addAll(photoFrame.toImage())
            }
            imageAdapter.addListPhoto(imagesList)
        }


    }
    fun initial(){

        imageAdapter = ImageAdapter(this,images_list)
        binding.recyclerView.adapter = imageAdapter

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager

    }
}