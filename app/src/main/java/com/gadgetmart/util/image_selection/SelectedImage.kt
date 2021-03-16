package com.gadgetmart.util.image_selection

import android.text.TextUtils


class SelectedImage(private val imagePath: String) {

    fun hasValidImagePath(): Boolean {
        return !TextUtils.isEmpty(imagePath)
    }
}