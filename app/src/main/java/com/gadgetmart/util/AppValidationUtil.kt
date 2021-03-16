package com.gadgetmart.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.text.isDigitsOnly
import java.io.ByteArrayOutputStream
import java.net.URISyntaxException
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppValidationUtil {

    companion object {

        fun isProfileImage(personName: String?): Boolean {

            if (TextUtils.isEmpty(personName)) {
                return false
            }

            return true
        }

        fun isEmpty(text: String?): Boolean {
            return text == null || text.trim { it <= ' ' }.isEmpty()
        }

        fun isNotEmpty(text: String?): Boolean {
            return text != null && text.trim { it <= ' ' }.isNotEmpty()
        }

        fun isValidName(text: String?): Boolean {
            return isNotEmpty(text) && !TextUtils.isDigitsOnly(
                text?.trim { it <= ' ' }
            )
        }

        fun isValidPersonName(personName: String?): Boolean {

            if (TextUtils.isEmpty(personName)) {
                return false
            }

            return true
        }

        fun isValidEmailId(emailId: String): Boolean {

            if (TextUtils.isEmpty(emailId)) {
                return false
            }

            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
        }

        fun isValidPhoneNumber(phoneNumber: String?): Boolean {

            if (TextUtils.isEmpty(phoneNumber)) {
                return false
            }

            if (phoneNumber!!.length < 10) {
                return false
            }

            if (!phoneNumber.isDigitsOnly()){
                return false
            }

            return true
        }

        fun isValidPassword(password: String?): Boolean {

            if (TextUtils.isEmpty(password)) {
                return false
            }

            if (password!!.length <= 6) {
                return false
            }

            return true
        }


        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }

        @Throws(URISyntaxException::class)
        fun getFilePath(context: Context, uri: Uri): String? {
            var uri = uri
            var selection: String? = null
            var selectionArgs: Array<String>? = null
            // Uri is different in versions after KITKAT (Android 4.4), we need to
            if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                    context.applicationContext,
                    uri
                )
            ) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("image" == type) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
            if ("content".equals(uri.scheme!!, ignoreCase = true)) {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver
                        .query(uri, projection, selection, selectionArgs, null)
                    val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }
                } catch (e: Exception) {
                }

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }
            return null
        }
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun getRealPathFromURI(contentUri: Uri, activity: Activity): String {
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor =
                    activity.getContentResolver().query(contentUri, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor!!.moveToFirst()
                return cursor!!.getString(column_index)
            } finally {
                if (cursor != null) {
                    cursor!!.close()
                }
            }
        }

        // slide the view from below itself to the current position
        fun slideUp(view: View) {
            view.visibility = View.VISIBLE
            val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                view.height.toFloat(),  // fromYDelta
                0F
            ) // toYDelta
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }

        // slide the view from its current position to below itself
        fun slideDown(view: View) {
            val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                0F,  // fromYDelta
                view.height.toFloat()
            ) // toYDelta
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }

        fun validateLetters(txt: String?): Boolean {
            val regx = "^[\\p{L} .'-]+$"
            val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(txt)
            return matcher.find()
        }

        fun validateAddress(txt: String?): Boolean {
            val regx = "!v.match(/[!@\$%^&*(),?\":{}|<>]/g)"
            val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(txt)
            return matcher.find()
        }

        fun gstinAddress(txt: String?): Boolean {
            val regx = "[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}\$"
            val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(txt)
            return matcher.find()
        }
    }

}