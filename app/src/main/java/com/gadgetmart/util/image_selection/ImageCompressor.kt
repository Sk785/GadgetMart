package com.gadgetmart.util.image_selection

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * A class used to compress an image.
 *
 * I've made this class very final and static, you can add Setters to change values.
 */
class ImageCompressor
/**
 * Constructor
 */(context: Activity?) {

    private val TAG = ImageCompressor::class.java.simpleName

    /**
     * Max width/height of the compressed image, whichever is achieved first
     */
    private val MAX_WIDTH_OR_HEIGHT = 600

    /**
     * In case, we are unable to find exact size-proportions of image, use
     * this width
     */
    private var MAX_WIDTH = 612

    /**
     * In case, we are unable to find exact size-proportions of image, use
     * this height
     */
    private var MAX_HEIGHT = 816

    /**
     * Compression format: JPEG
     */
    private val compressFormat = Bitmap.CompressFormat.JPEG

    /**
     * Compression Quality: 80%
     */
    private val quality = 80

    /**
     * Answer to question: Where to save to compressed file?
     */
    private val destinationDirectoryPath: String

    /**
     * If we need File after compression, use this callback
     */
    private val imageCallback: ImageCallback? = null

    init {

        // Save compressed images in this directory
        destinationDirectoryPath = "" +
                context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +
                File.separator +
                "compressed_images"
    }

    /*
     * Definition Methods
     */

    /**
     * Compress an image from given path to a file
     *
     * @param imagePath path od image
     * @return Compressed file
     * @throws IOException unable to load image file into memory, or can't find it
     */
    @Throws(IOException::class)
    fun compressToFile(imagePath: String): File? {

        // set width/height according to image resolution
        setMaxResolutionFromImage(imagePath)

        // Get file from path
        val file = File(imagePath)

        // proceed to compress
        return compressImage(
            file, MAX_WIDTH, MAX_HEIGHT, compressFormat, quality,
            destinationDirectoryPath + File.separator + file.name
        )
    }

    /*
     * Work Methods
     */

    /**
     * Find exact resolution of image, and set it near to [.MAX_WIDTH_OR_HEIGHT]
     *
     * @param imagePath image path
     */
    private fun setMaxResolutionFromImage(imagePath: String) {

        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, bitmapOption)

        val resolution = Resolution(
            bitmapOption.outWidth, bitmapOption.outHeight
        ).sampleSize()

        if (bitmapOption.outWidth > 0 && bitmapOption.outHeight > 0) {
            MAX_WIDTH = resolution.width
            MAX_HEIGHT = resolution.height
        } else {
            // ignored, we will use default 612x816
        }
    }

    /**
     * Compress the image from file using all settings
     *
     * @param imageFile image file to be compressed
     * @param reqWidth max width of image file
     * @param reqHeight max height of image file
     * @param compressFormat compression format
     * @param quality compression quality
     * @param destinationPath destination to save compressed image
     * @return Compressed File
     * @throws IOException unable to find or load file into memory
     */
    @Throws(IOException::class)
    private fun compressImage(
        imageFile: File, reqWidth: Int, reqHeight: Int,
        compressFormat: Bitmap.CompressFormat, quality: Int,
        destinationPath: String
    ): File? {

        // create save path
        var fileOutputStream: FileOutputStream? = null
        val file = File(destinationPath).parentFile
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d(TAG, "unable to create file path for saving file.")
                return null
            }
        }


        try {
            fileOutputStream = FileOutputStream(destinationPath)

            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(
                compressFormat,
                quality,
                fileOutputStream
            )
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }

        return File(destinationPath)
    }

    /**
     * Convert file to sampled bitmap (compressed)
     *
     * @param imageFile image file
     * @param reqWidth max width
     * @param reqHeight max height
     * @return Decoded bitmap
     * @throws IOException unable to locate or load file into memory
     */
    @Throws(IOException::class)
    private fun decodeSampledBitmapFromFile(
        imageFile: File,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        //check the rotation of the image and display it properly
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = Matrix()
        when (orientation) {
            6 -> matrix.postRotate(90F)
            3 -> matrix.postRotate(180F)
            8 -> matrix.postRotate(270F)
        }
        scaledBitmap = Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
        return scaledBitmap
    }

    /**
     * Used to calculate sample/compress size that can be placed onto
     * memory
     *
     * @param options conversion options as [BitmapFactory.Options]
     * @param reqWidth max width
     * @param reqHeight max height
     * @return calculated sample size
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {

        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Image Resolution: width & height
     */
    internal inner class Resolution(var width: Int, var height: Int) {

        /**
         * From a given size, increase/decrease the size uniformally
         * to match it with [.MAX_WIDTH_OR_HEIGHT]
         *
         * @return a [Resolution] of calculated image size
         */
        fun sampleSize(): Resolution {

            Log.d(TAG, "Original Resolution: " + toString())

            if (width < MAX_WIDTH_OR_HEIGHT || height < MAX_WIDTH_OR_HEIGHT) {

                while (width < MAX_WIDTH_OR_HEIGHT && height < MAX_WIDTH_OR_HEIGHT) {
                    setWidth(++width)
                    setHeight(++height)
                }
            } else if (width > MAX_WIDTH_OR_HEIGHT || height > MAX_WIDTH_OR_HEIGHT) {

                while (width > MAX_WIDTH_OR_HEIGHT && height > MAX_WIDTH_OR_HEIGHT) {
                    setWidth(--width)
                    setHeight(--height)
                }
            }

            Log.d(TAG, "Sampled Resolution: " + toString())
            return this
        }

        fun setWidth(width: Int): Resolution {
            this.width = width
            return this
        }

        fun setHeight(height: Int): Resolution {
            this.height = height
            return this
        }

        override fun toString(): String {
            return "Resolution{" +
                    "width=" + width +
                    ", height=" + height +
                    '}'.toString()
        }
    }

    /**
     * A listener to get Compressed File
     */
    interface ImageCallback {

        fun onImageCompressed(file: File)
    }
}