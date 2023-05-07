package com.example.image_application_in_kotlin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.example.image_application_in_kotlin.R
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

/**
 *  Create by TruongIT
 */

object FileUtil {
    /**
     *  - Khởi tạo thư mục lưu trữ ảnh của ứng dụng trên thiết bị.
     *  - (Environment.getExternalStorageDirectory().absolutePath), hằng số
     *  - Environment.DIRECTORY_PICTURES đại diện cho thư mục ảnh, và tên thư mục chứa ảnh của ứng dụng (/MyPic).
     * */
    val pictureDirectory =
        File(Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_PICTURES + "/MyPic")

    // Kiểm tra xem một tệp tin có tồn tại trong thư mục lưu trữ ảnh của ứng dụng hay không
    fun isFileExisted(fileName: String): Boolean {

        // biến path đại diện cho đường dẫn tuyệt đối đến tệp tin cần kiểm tra trong thư mục pictureDirectory
        // Phương thức này sẽ tạo ra các thư mục con nếu cần thiết để tạo được đường dẫn đầy đủ đến thư mục mới.
        val path = "${pictureDirectory}/${fileName}"
        Log.d("existed", File(path).exists().toString())
        return File(path).exists()
    }

    // init{} được sử dụng để tạo thư mục lưu trữ ảnh của ứng dụng trên thiết bị nếu nó chưa tồn tại.
    init {
        // Nếu thư mục không tồn tại, thì khởi tạo sử dụng phương thức mkdirs() của đối tượng File để tạo thư mục.
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs()
        }
    }

    fun songArt(path: String, context: Context): Bitmap {
        val retriever = MediaMetadataRetriever()
        val inputStream: InputStream
        retriever.setDataSource(path)
        if (retriever.embeddedPicture != null) {
            inputStream = ByteArrayInputStream(retriever.embeddedPicture)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            retriever.release()
            return bitmap
        } else {
            // Lấy đối tượng Drawable từ resources
            val drawable = context.resources.getDrawable(R.drawable.c)

// Chuyển đổi Drawable thành Bitmap
            val bitmap = (drawable as BitmapDrawable).bitmap
            return bitmap
        }
    }

}