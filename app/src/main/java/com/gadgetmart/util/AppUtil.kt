package com.gadgetmart.util

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AppUtil {

    companion object {

        const val ringCaptchaApiKey = "690ea9a39369447b26c1b9080801a577716cb849"
        const val ringCaptchaAppKey = "zi4yjede1i1a5a3ylo5u"

        const val awsAccessKeyId = ""
        const val awsSecretAccessKey = ""
        const val s3Bucket = "gadgetszone"
        const val s3BucketFileName = "gadgetzone/images/users"
        const val s3BucketProductName = "gadgetzone/user_product_images"


        const val GetWishListFlag = 0
        const val RemoveWishListFlag = 1
        const val GetAddressesFlag = 2
        const val RemoveAddressFlag = 3
        const val AddAddressFlag = 4
        const val EditAddressFlag = 5
        const val GetPopularProductsFlag = 6
        const val GetOfferProductsFlag = 7
        const val GetRegularProductsFlag = 8
//        const val ringCaptchaSecretKey = "makateqi2ona1uvypo4a"

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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

            if (phoneNumber!!.length < 10) {
                return false
            }

            if (!phoneNumber.isDigitsOnly()) {
                return false
            }

            return true
        }

        fun isFieldEmpty(text: String?): Boolean {
            if (TextUtils.isEmpty(text)) {
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
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }


        fun getRealPathFromURI(contentUri: Uri, activity: Activity): String {
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor =
                    activity.contentResolver.query(contentUri, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        }

        private fun uploadAFile(context: Context, file: File, image_name: String) {
            val credentials = BasicAWSCredentials(
                AppUtil.awsAccessKeyId,
                AppUtil.awsSecretAccessKey
            )
            val s3Client = AmazonS3Client(credentials)
            s3Client.setRegion(Region.getRegion(Regions.US_EAST_2))
            val transferUtility: TransferUtility = TransferUtility.builder()
                .context(context)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(s3Client)
                .build()
//            image_name = "pic" + System.currentTimeMillis() + ".png"
            val uploadObserver: TransferObserver =
                //transferUtility.upload("lidiomuploads/" ,name, file);
                transferUtility.upload(
                    "gadgetszone/images/users",
                    image_name,
                    file,
                    CannedAccessControlList.PublicRead
                )
            uploadObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    Log.e("state", state.name)
                    if (TransferState.COMPLETED === state) { //pd.dismiss();
//                        GlobalConstants.dismissProgressDialog()
                        Log.e(
                            "filesend2",
                            "https://ec2-54-255-146-196.ap-southeast-1.compute.amazonaws.com/gadgetszone/images/users/$image_name"
                        )

                    } else if (state.name == "FAILED") {
                        Log.e("image_path", "failed")
                    }
                }

                override fun onProgressChanged(
                    id: Int,
                    bytesCurrent: Long,
                    bytesTotal: Long
                ) {
                    val percentDonef =
                        bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                    val percentDone = percentDonef.toInt()
                    Log.e("fileUpload", "" + percentDone)
                }

                override fun onError(id: Int, ex: Exception) { //  pd.dismiss();
//                    GlobalConstants.dismissProgressDialog()
                    Log.e("exception", ex.toString())
                }
            })
        }

        private fun getCountries() {
            val locales: Array<Locale> = Locale.getAvailableLocales()
            val countries = ArrayList<String>()
            for (locale in locales) {
                val country: String = locale.getDisplayCountry()
                if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                    countries.add(country)
                }
            }

            countries.sort()
            for (country in countries) {
                println(country)
            }
        }

        fun showSoftKeyboard(context: Context) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideSoftKeyboard(context: Context, view: View) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        fun roundTwoDecimalPlaces(
            context: Context?,
            d: Double
        ): Double {
            val value: Double
            value = Math.round(d * 100.0) / 100.0
            return value
        }

        fun roundTwoInt(
            context: Context?,
            d: Double
        ): Int {
//            val value: Double
//            value = Math.round(d * 100.0) / 100.0

            val a = Math.round(d).toInt()
            return a
        }

        fun firebaseEvent(
            context: Context,
            propertyName: String,
            event: String,
            propertyValue: String
        ) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString(propertyName, propertyValue)
            firebaseAnalytics?.logEvent(event, bundle)
        }
        fun firebaseEventForAddress(
            context: Context,
            propertyName: String,
            propertyNameSecond: String,
            event: String,
            propertyValue: String,
            propertyValueSecond: String
        ) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString(propertyName, propertyValue)
            bundle.putString(propertyNameSecond, propertyValueSecond)

            firebaseAnalytics?.logEvent(event, bundle)
        }


    }

}