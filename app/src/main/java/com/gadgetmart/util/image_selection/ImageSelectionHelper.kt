package com.gadgetmart.util.image_selection

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gadgetmart.util.permission.PermissionSupport
import java.io.FileNotFoundException
import java.io.IOException


class ImageSelectionHelper(
    activity: Activity?,
    permissionSupport: PermissionSupport,
    callback: ImageSelectorFragmentSheet.Callback
) : ImageSelectorFragmentSheet.Callback {

    companion object {

        private val TAG = ImageSelectionHelper::class.java.simpleName

        private val RC_TAKE_PICTURE = 10001
        private val RC_TAKE_PICTURE_PERM = 10002

        private val RC_CHOOSE_FROM_GALLERY = 10003
        private val RC_CHOOSE_FROM_GALLERY_PERM = 10004

    }

    private var activity: Activity? = activity
    private var fragment: Fragment? = null
    private var permissionSupport: PermissionSupport? = permissionSupport

    private var imageUri: Uri? = null
    private var imageUriHelper: ImageUriHelper? = null
    private var compressor: ImageCompressor? = null

    private var selectionTag = -1
    private var imageSelectorFragmentSheet: ImageSelectorFragmentSheet? = null


    /*
     * Image Selection and Processing
     */

    fun setFragmentModeUsing(fragment: Fragment): ImageSelectionHelper {
        this.fragment = fragment
        return this
    }

    fun setSelectionTag(selectionTag: Int): ImageSelectionHelper {
        this.selectionTag = selectionTag
        return this
    }

    fun showImageSelector(fragmentManager: FragmentManager) {

        if (imageSelectorFragmentSheet != null) {
            imageSelectorFragmentSheet!!.show(fragmentManager, "select_purchase_images")
        } else {
            callback!!.onImageSelectionFailed("Can't show Image Selection Sheet.", selectionTag)
        }
    }

    fun showImageSelector(fragmentManager: FragmentManager?, selectionTag: Int) {

        this.selectionTag = selectionTag

        if (imageSelectorFragmentSheet != null) {
            imageSelectorFragmentSheet!!.show(fragmentManager!!, "select_purchase_images")
        } else {
            callback!!.onImageSelectionFailed("Can't show Image Selection Sheet.", selectionTag)
        }
    }


    /*
     * ImageSelectorFragmentSheet.Callback
     */

    override fun onImageSourceSelected(optionId: Int) {

        when (optionId) {

            ImageSelectorFragmentSheet.SELECTED_FILE_CAMERA ->

                permissionSupport!!.checkAndRequestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), false, RC_TAKE_PICTURE_PERM
                )

            ImageSelectorFragmentSheet.SELECTED_FILE_GALLERY ->

                permissionSupport!!.checkAndRequestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), false, RC_CHOOSE_FROM_GALLERY_PERM
                )
        }
    }

    fun onTakePictureSelected() {

        try {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            imageUri = activity!!.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )

            val starter = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            starter.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            if (fragment == null) {
                activity!!.startActivityForResult(starter, RC_TAKE_PICTURE)
            } else {
                fragment!!.startActivityForResult(starter, RC_TAKE_PICTURE)
            }
        } catch (e: Exception) {
            callback!!.onImageSelectionFailed("Take Picture Exception: " + e.message, selectionTag)
        }

    }

    fun onChooseFromGallerySelected() {

        try {
            val starter = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            if (fragment == null) {
                activity!!.startActivityForResult(starter, RC_CHOOSE_FROM_GALLERY)
            } else {
                fragment!!.startActivityForResult(starter, RC_CHOOSE_FROM_GALLERY)
            }
        } catch (e: Exception) {
            callback!!.onImageSelectionFailed(
                "Choose From Gallery Exception: " + e.message,
                selectionTag
            )
        }

    }

    private fun processSelectedOrTakenPictures(requestCode: Int, data: Intent?) {

        var imagePath: String? = null
        if (requestCode == RC_TAKE_PICTURE) {
            imagePath = imageUriHelper!!.getRealPathFromURI(imageUri)
        } else if (requestCode == RC_CHOOSE_FROM_GALLERY) {

            if (data != null && data.data != null) {
                Log.d(TAG, "processSelectedOrTakenPictures() " + data.data!!)
                imagePath = imageUriHelper!!.getRealPathFromURI(data.data!!)
            } else {
                Log.d(TAG, "RC_CHOOSE_FROM_GALLERY: Selected Image not found.")
            }
        }

        compressAndPostSelectedImage(imagePath)
    }

    private fun compressAndPostSelectedImage(imagePath: String?) {

        if (imagePath == null) {
            callback!!.onImageSelectionFailed("Selected Image not found.", selectionTag)
            return
        }

        /*
         * Compress first, right?
         */

        try {

            val compressedImagePath = compressor!!.compressToFile(imagePath)!!.path
            callback!!.onImageSelected(SelectedImage(compressedImagePath), selectionTag)
        } catch (e: IOException) {
            Log.d(TAG, "Compressor Exception: " + e.message)
            e.printStackTrace()

            if (e is FileNotFoundException) {
                callback!!.onImageSelectionFailed("File not found.", selectionTag)
            }
        }

    }

    /*
     * Permission Result
     */

    fun reportActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {

        if (resultCode == RESULT_OK && (requestCode == RC_TAKE_PICTURE || requestCode == RC_CHOOSE_FROM_GALLERY)) {

            processSelectedOrTakenPictures(requestCode, data)
            return true
        } else {
            return false
        }
    }

    fun reportPermissionResult(isAllowed: Boolean, who: Int) {

        if (isAllowed) {

            if (who == RC_TAKE_PICTURE_PERM) {
                onTakePictureSelected()
            } else if (who == RC_CHOOSE_FROM_GALLERY_PERM) {
                onChooseFromGallerySelected()
            }
        } else {
            callback!!.onImageSelectionFailed(
                "This feature will not work without permissions.",
                selectionTag
            )
        }
    }

    /*
     * ErrorCallback
     */

    var callback: Callback? = null

    interface Callback {

        fun onImageSelected(selectedImage: SelectedImage, who: Int)

        fun onImageSelectionFailed(error: String, who: Int)
    }

    init {
        imageUriHelper = ImageUriHelper(activity)
        compressor = ImageCompressor(activity)
        imageSelectorFragmentSheet = ImageSelectorFragmentSheet.newInstance()
        imageSelectorFragmentSheet!!.setSelectionListener(this)
    }

}