package com.example.image_application_in_kotlin.utils

import android.content.Context
import com.google.android.material.shape.MaterialShapeDrawable

/**
 *  Create by TruongIT
 */

object UtilView {
    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}