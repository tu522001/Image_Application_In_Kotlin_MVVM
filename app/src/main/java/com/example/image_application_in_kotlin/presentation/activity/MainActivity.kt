package com.example.image_application_in_kotlin.presentation.activity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.image_application_in_kotlin.R
import com.example.image_application_in_kotlin.data.model.Images
import com.example.image_application_in_kotlin.data.model.PhotoFrames
import com.example.image_application_in_kotlin.databinding.ActivityMainBinding
import com.example.image_application_in_kotlin.presentation.adapter.ImageAdapter
import com.example.image_application_in_kotlin.presentation.viewmodel.ImageViewModel
import com.example.image_application_in_kotlin.utils.FileUtil
import java.io.File

/**
 *  Create by TruongIT
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
//    private  var images_list : MutableList<Images>
    private var images_list = mutableListOf<Images>()
    private lateinit var layoutManager: LinearLayoutManager
    private var imageItem: Images? = null

    private var imagesViewModel: ImageViewModel? = null
    private var downloadID: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Sử dụng biến binding để truy cập các thành phần trong layout
        setContentView(binding.root)
//        images_list = mutableListOf<Images>()
        initial()

        // Khởi tạo đối tượng ImageViewModel
        imagesViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        imagesViewModel!!.getListPhoto()
        imagesViewModel!!.photoList.observe(this) { photoList ->
            images_list = mutableListOf<Images>()
            for (photoFrame in photoList) {
                images_list.addAll(photoFrame.toImage())
            }
            imageAdapter.addListPhoto(images_list)
            Log.d("AAAS","photoList : "+photoList)
            Log.d("AAAS","imagesList : "+images_list)

        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollToRelativeCategory()
            }
        })

        imageAdapter.setOnItemClick(object : ImageAdapter.OnItemListener {
            override fun onClick(position: Int, url: String) {
                Log.d("YYY", "position MainActivity: " + position + ", URL : " + url)
                imageItem = images_list[position]
                if (imageItem != null && FileUtil.isFileExisted(imageItem!!.fileName)) {
                    displayImage()
                }
            }
        })

        imageAdapter.setDownloadClick(object : ImageAdapter.OnDownloadClickListener {
            override fun onDownloadClick(downloadId: Long) {
                Log.d("FGH", "downloadId MainActivity: $downloadId")
                // Xử lý dữ liệu downloadId ở đây
                downloadID = downloadId
            }
        })

        binding.button.setOnClickListener {
            binding.imgAvatar.setImageResource(R.drawable.img)
        }

    }
    fun initial(){
        imageAdapter = ImageAdapter(this, images_list)
        binding.recyclerView.adapter = imageAdapter
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager

    }

    private fun displayImage() {
        imageItem?.let {
            Glide.with(this).load(it.url).into(binding.imgAvatar)
            binding.txtName.text = it.fileName
            imageAdapter.notifyDataSetChanged()
        }
    }

    fun scrollToRelativeCategory() {
        val lastVisibleFrame = layoutManager.findLastVisibleItemPosition()
        if (lastVisibleFrame > -1) {
            val image: Images = images_list[lastVisibleFrame]
            images_list.forEach {
                if (it.folder.equals(image.folder)) {
                    binding.recyclerViewText.smoothScrollToPosition(
                        images_list.indexOf(it)
                    )
//                    textAdapter.selectedPosition = (photoFramesList.indexOf(it))
                }
            }
        }
    }

    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                // Lấy ID của download
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.d("YUY", "downloadID" + downloadID)
                Log.d("YUY", "downloadId" + downloadId)
                if (downloadID == downloadId) {

                    displayImage()
                    imageAdapter.notifyDataSetChanged()
                    // Xử lý khi download hoàn thành
                    // Ví dụ: hiển thị thông báo hoặc mở file đã tải về
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        val intent = Intent(this, MusicService::class.java)
//        bindService(intent, this, BIND_AUTO_CREATE)
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteReceiver, intentFilter)

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(downloadCompleteReceiver)
//        unbindService(this)
    }

}