package com.example.image_application_in_kotlin.data.remote

/**
 *  Create by TruongIT
 */

open class AppResource<T> {
    var data: T? = null
    var message: String? = null
    var status: Status? = null

    constructor(data: T?, status: Status?) {
        this.data = data
        this.status = status
    }

    constructor(message: String?, status: Status?) {
        this.message = message
        this.status = status
    }

    class Success<T>(data: T?) : AppResource<T>(data, Status.SUCCESS)
    class Error<T>(message: String?) : AppResource<T>(message, Status.ERROR)
    class Loading<T>(data: T?) : AppResource<T>(data, Status.LOADING)

    enum class Status {
        SUCCESS, LOADING, ERROR
    }
}
