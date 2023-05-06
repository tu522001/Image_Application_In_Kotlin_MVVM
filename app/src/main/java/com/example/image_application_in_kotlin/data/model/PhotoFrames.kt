package com.example.image_application_in_kotlin.data.model

data class PhotoFrames(
    val cover: String,
    val defines: List<Define>,
    val folder: String,
    val icon: String,
    val lock: Boolean,
    val name: String,
    val name_vi: String,
    val openPackageName: String,
    val totalImage: Int
){
    fun toImage() : List<Images> {
        val images = mutableListOf<Images>()

        defines.forEach { defineX ->
            for (i in defineX.start until defineX.end) {
                images.add(
                    Images(
                        url = "https://mystoragetm.s3.ap-southeast-1.amazonaws.com/Frames/ClassicFrames/" + folder + "/" + folder + "_frame_" + i+ ".png",
                        isEnd = i == defines.size - 1, folder = folder, fileName =  folder + "_frame_" + i+ ".png"
                    )
                )
            }
        }


        return images
    }
}
