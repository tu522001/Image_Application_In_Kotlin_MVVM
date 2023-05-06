package com.example.image_application_in_kotlin.data.model

data class Images(
    var url: String = "",
    var isFirst: Boolean = false,
    var isEnd: Boolean = false,
    var isSpace: Boolean = false,
    var folder: String = "",
    var downloaded: Boolean = false,
    var fileName : String = ""
)