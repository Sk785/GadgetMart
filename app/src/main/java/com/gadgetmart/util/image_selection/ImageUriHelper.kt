package com.gadgetmart.util.image_selection

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore


internal class ImageUriHelper(private val activity: Activity?) {

    fun getRealPathFromURI(contentUri: Uri?): String? {

        try {
            var res: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity!!.contentResolver.query(contentUri!!, proj, null, null, null)
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(columnIndex)
            }
            cursor.close()
            return res

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}