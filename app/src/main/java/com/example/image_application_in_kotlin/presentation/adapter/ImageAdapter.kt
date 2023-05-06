package com.example.image_application_in_kotlin.presentation.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.image_application_in_kotlin.data.model.Images
import com.example.image_application_in_kotlin.data.model.PhotoFrames
import com.example.image_application_in_kotlin.databinding.ItemIconBinding
import com.example.image_application_in_kotlin.utils.Util


class ImageAdapter(var context: Context, var listImages: MutableList<Images>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var selectedPosition = -1
    private var urlList = mutableListOf<String>()
    private lateinit var onItemListener: OnItemListener


    fun addListPhoto(data: MutableList<Images>) {
        if (listImages.isNotEmpty()) {
            listImages.clear()
        }
        listImages.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(val itembinding: ItemIconBinding) :
        RecyclerView.ViewHolder(itembinding.root) {


        fun bind(image: Images) {
            

            Glide.with(itembinding.root).load(image.url).into(itembinding.imgcCoverPhoto)
            Log.d("III", "itembinding.textView2.text = image.url : " + image.url)

//            // Thiết lập ShapeAppearanceModel cho MaterialShapeDrawable
            if (((position + 1) % 6 == 0)) { // Bo góc ở vị trí 6, 12, 18,...
                // kích thước bo góc 14dpToPx
                val cornerSize = 14.dpToPx(itembinding.root.context)
                val shapeAppearanceModel =
                    itembinding.imgcCoverPhoto.shapeAppearanceModel.toBuilder()

                        //bo góc trên và dưới ở bên phải
                        .setTopRightCornerSize(cornerSize.toFloat())
                        .setBottomRightCornerSize(cornerSize.toFloat()).build()
                itembinding.imgcCoverPhoto.shapeAppearanceModel = shapeAppearanceModel
                itembinding.imageViewBlur.shapeAppearanceModel = shapeAppearanceModel
            } else if (((position + 1) % 6 == 1)) {
                val cornerSize = 14.dpToPx(itembinding.root.context)
                val shapeAppearanceModel =
                    itembinding.imgcCoverPhoto.shapeAppearanceModel.toBuilder()
                        //bo góc trên và dưới ở bên trái
                        .setTopLeftCornerSize(cornerSize.toFloat())
                        .setBottomLeftCornerSize(cornerSize.toFloat()).build()
                itembinding.imgcCoverPhoto.shapeAppearanceModel = shapeAppearanceModel
                itembinding.imageViewBlur.shapeAppearanceModel = shapeAppearanceModel
            }

            urlList.add(image.url)

            // click vào ảnh con
            itembinding.imgcCoverPhoto.setOnClickListener {

                selectItem(adapterPosition)
                onItemListener.onClick(adapterPosition, listImages[adapterPosition].url)

                if (!Util.isFileExisted(image.fileName)) {

                    // Chưa tải về, đặt hình từ URL và tải về khi nhấp chuột
                    Glide.with(context).load(image.url).into(itembinding.imgcCoverPhoto)

                    // ẩn hình download đi
                    // Tải hình về
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val request = DownloadManager.Request(Uri.parse(image.url))
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        .setTitle(image.url)
                        .setDescription("Đang tải xuống ${image.url}")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_PICTURES,
                            "MyPic/${image.fileName}"
                        )
                    itembinding.imageViewProgressbar.visibility = View.VISIBLE
                }
            }
        }

        private fun selectItem(position: Int) {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }

        fun Int.dpToPx(context: Context): Int {
            val density = context.resources.displayMetrics.density
            return (this * density).toInt()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ImageAdapter.ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var itemBinding = ItemIconBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.bind(listImages[position])
        Log.d("NNN", "listImages[position].url : " + listImages[position].url)
        if (position > 0 && (position + 1) % 6 == 0) {
            // Nếu là ảnh thứ 6, thêm view trống
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.setMargins(0, 0, 20, 0) // margin phải 16dp
            holder.itemView.layoutParams = layoutParams
        } else {
            // Nếu không phải ảnh thứ 6, xoá margin
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            layoutParams.setMargins(0, 0, 0, 0)
            holder.itemView.layoutParams = layoutParams
        }
    }

    override fun getItemCount(): Int {
        return listImages.size
        Log.d("SDD", "listImages.size : " + listImages.size)
    }

    interface OnItemListener {
        fun onClick(position: Int, url: String)
    }


}