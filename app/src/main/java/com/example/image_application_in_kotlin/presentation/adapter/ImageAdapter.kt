package com.example.image_application_in_kotlin.presentation.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.image_application_in_kotlin.data.model.DownloadImages
import com.example.image_application_in_kotlin.data.model.Images
import com.example.image_application_in_kotlin.databinding.ItemIconBinding
import com.example.image_application_in_kotlin.utils.FileUtil
import com.example.image_application_in_kotlin.utils.UtilView

/**
 *  Create by TruongIT
 */

class ImageAdapter(var context: Context, var listImages: MutableList<Images>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var selectedPosition = -1
    private var urlList = mutableListOf<String>()
    private val downloadedImages = mutableListOf<DownloadImages>()
    private lateinit var listener: OnDownloadClickListener
    private lateinit var onItemListener: OnItemListener

    fun addListPhoto(data: MutableList<Images>) {
        listImages.clear()
        listImages.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(val itembinding: ItemIconBinding) :
        RecyclerView.ViewHolder(itembinding.root) {

        fun bind(image: Images) {
            itembinding.apply {

                // Kiểm tra xem tập tin hình ảnh có tồn tại trong thiết bị hay  chưa
                if (FileUtil.isFileExisted(image.fileName)) {
                    // nếu đã có ảnh trong thư mục rồi thì ẩn icon download đi
                    itembinding.download.visibility = View.GONE

                } else {
                    // còn chưa có ảnh trong thư mục thì hiển thị icon download lên
                    itembinding.download.visibility = View.VISIBLE
                }

                // Kiểm tra xem item hiện tại có được chọn hay không bằng cách so sánh
                if (selectedPosition == adapterPosition) {
                    // Nếu item được chọn, thì kiểm tra xem tập tin hình ảnh có tồn tại hay không
                    if (FileUtil.isFileExisted(image.fileName)) {
                        itembinding.imageViewload.visibility = View.VISIBLE
                        itembinding.imageViewBlur.visibility = View.VISIBLE
                        itembinding.imageViewProgressbar.visibility = View.GONE
                    } else {
                        itembinding.imageViewload.visibility = View.GONE
                        itembinding.imageViewBlur.visibility = View.GONE
                        itembinding.imageViewProgressbar.visibility = View.VISIBLE
                    }
                } else {
                    //Nếu item không được chọn, thì ẩn hình ảnh
                    itembinding.imageViewload.visibility = View.GONE
                    itembinding.imageViewBlur.visibility = View.GONE
                    itembinding.imageViewProgressbar.visibility = View.GONE
                }

                // Hiển thị hình ảnh item bằng Glide
                Glide.with(itembinding.root).load(image.url).into(itembinding.imgcCoverPhoto)

//            // Thiết lập ShapeAppearanceModel cho MaterialShapeDrawable
                if (((position + 1) % 6 == 0)) { // Bo góc ở vị trí 6, 12, 18,...
                    // kích thước bo góc 14dpToPx
                    val cornerSize = UtilView.dpToPx(itembinding.root.context, 14)
                    val shapeAppearanceModel =
                        itembinding.imgcCoverPhoto.shapeAppearanceModel.toBuilder()
                            //bo góc trên và dưới ở bên phải
                            .setTopRightCornerSize(cornerSize.toFloat())
                            .setBottomRightCornerSize(cornerSize.toFloat()).build()
                    itembinding.imgcCoverPhoto.shapeAppearanceModel = shapeAppearanceModel
                    itembinding.imageViewBlur.shapeAppearanceModel = shapeAppearanceModel
                } else if (((position + 1) % 6 == 1)) {
                    // kích thước bo góc 14dpToPx
                    val cornerSize = UtilView.dpToPx(itembinding.root.context, 14)
                    val shapeAppearanceModel =
                        itembinding.imgcCoverPhoto.shapeAppearanceModel.toBuilder()
                            //bo góc trên và dưới ở bên trái
                            .setTopLeftCornerSize(cornerSize.toFloat())
                            .setBottomLeftCornerSize(cornerSize.toFloat()).build()
                    itembinding.imgcCoverPhoto.shapeAppearanceModel = shapeAppearanceModel
                    itembinding.imageViewBlur.shapeAppearanceModel = shapeAppearanceModel
                }
            }
            // Thêm một đường dẫn URL tải về hình ảnh vào một danh sách urlList
            urlList.add(image.url)

            // Khi người dùng nhấp vào một hình ảnh con trong danh sách hình ảnh trên giao diện người dùng.
            itembinding.imgcCoverPhoto.setOnClickListener {
                // Khi người dùng nhấp vào hình ảnh, phương thức selectItem(adapterPosition) được gọi để đánh dấu mục được chọn trong danh sách hiện tại.

                // Được gọi để thông báo cho trình nghe sự kiện về việc một mục đã được chọn.
                try {
                    selectItem(adapterPosition)
                    if (listImages.size > -1){
                        onItemListener.onClick(adapterPosition, listImages[adapterPosition].url)
                        // Nếu hình ảnh chưa được tải xuống và lưu vào thiết bị di động
                        if (!FileUtil.isFileExisted(image.fileName)) {
                            Glide.with(context).load(image.url).into(itembinding.imgcCoverPhoto)

                            // Sử dụng lớp DownloadManager để tải hình xuống và lưu trữ vào thiết bị di động.
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


                            val downloadId = downloadManager.enqueue(request)
                            listener.onDownloadClick(downloadId)
                            Log.d("EEE", "downloadId : " + downloadId)

                            // Thêm hình đã tải vào danh sách
                            val downloadedImage = DownloadImages(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path,
                                image.url
                            )
                            downloadedImages.add(downloadedImage)
                        }
                    }
                }catch (exception : Exception){
                    Log.d("Exception", "exception : " + exception.message)
                }

            }
        }

        /**
         * Đoạn code này là một phương thức trong Adapter của RecyclerView, được sử dụng để chọn một item trong danh sách.
         * Phương thức này nhận đầu vào là vị trí của item được chọn.
         * Trong phương thức này, đầu tiên kiểm tra xem vị trí được chọn đã được chọn trước đó hay chưa bằng cách so sánh với
         * RecyclerView.NO_POSITION. Nếu đã được chọn trước đó, sử dụng phương thức notifyItemChanged() để thông báo cho Adapter
         * rằng item đó cần được cập nhật lại trên giao diện người dùng.
         * Tiếp theo, gán vị trí được chọn cho biến selectedPosition, và cuối cùng gọi phương thức notifyItemChanged()
         * một lần nữa để cập nhật lại item đã được chọn trên giao diện người dùng.
         * Tóm lại, phương thức này đảm bảo rằng chỉ có một item được chọn một lúc
         * và hiển thị trên giao diện người dùng với các trạng thái và hiệu ứng tương ứng.
         */

        private fun selectItem(position: Int) {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = position
            notifyItemChanged(selectedPosition)
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

        // Xử lý khoảng cách
        if (position > 0 && (position + 1) % 6 == 0) {
            // Nếu là ảnh thứ 6, thêm view trống ( tức là cứ 6 ảnh thì 1 khoảng cách ra )
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
        if (listImages.isEmpty()) {
            return 0
        }
        return listImages.size
    }

    fun setOnItemClick(onItemListener: OnItemListener) {
        this.onItemListener = onItemListener
    }

    fun setDownloadClick(listener: OnDownloadClickListener) {
        this.listener = listener
    }

    interface OnItemListener {
        fun onClick(position: Int, url: String)
    }

    interface OnDownloadClickListener {
        fun onDownloadClick(downloadId: Long)
    }

}