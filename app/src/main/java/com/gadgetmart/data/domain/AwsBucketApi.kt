package com.gadgetmart.data.domain

import android.app.Activity
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.gadgetmart.R
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import java.io.File

class AwsBucketApi {
    companion object {
        // upload image file to s3 bucket
        fun uploadImageToBucket(
            activity: Activity?,
            mFile: File?,
            fileName: String?,
            bucketApiListener: AwsBucketApiListener
        ) {


            val objProgressDialog = activity?.let { CustomProgressDialog(it) }
            objProgressDialog?.dialogShow()
//            val imageCompressor = ImageCompressor(activity)
            val file = mFile


            val credentials =
                BasicAWSCredentials(activity!!.resources.getString(R.string.access_key), activity!!.resources.getString(R.string.secreat_key))
            val s3Client = AmazonS3Client(credentials)

            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1))
            val transferUtility = TransferUtility.builder()
                .context(activity)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .s3Client(s3Client)
                .build()

            val uploadObserver =
                transferUtility.upload(
                    AppUtil.s3BucketFileName,
                    fileName,
                    file,
                    CannedAccessControlList.PublicRead
                )


            uploadObserver.setTransferListener(object : TransferListener {

                override fun onStateChanged(id: Int, state: TransferState) {
                    Log.e("state", state.name)
                    if (TransferState.COMPLETED == state) {
                        val profilePath =
                            "https://s3.ap-south-1.amazonaws.com/gadgetzone/images/users/$fileName"
                        bucketApiListener.onPicUploadedOnBucket(profilePath)
                        objProgressDialog?.dialogDismiss()

                    } else if (state.name.equals("FAILED", ignoreCase = true)) {
                        bucketApiListener.onPicNotUploadedOnBucket("File uploading failed! Please try again")
                        objProgressDialog?.dialogDismiss()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val percent = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                    val percentDone = percent.toInt()
                    Log.e("fileUpload", "" + percentDone)
//                    progress_bar?.progress = percentDone
//                    progress_percentage_text?.text = "$percentDone/100"
                }

                override fun onError(id: Int, ex: Exception) {
                    //  pd.dismiss();
                    objProgressDialog?.dialogDismiss()
                    bucketApiListener.onPicNotUploadedOnBucket(ex.message)
                }
            })

        }


        fun uploadProductImageToBucket(
            activity: Activity?,
            mFile: File?,
            fileName: String?,
            bucketApiListener: AwsBucketApiListener
        ) {


            val objProgressDialog = activity?.let { CustomProgressDialog(it) }
            objProgressDialog?.dialogShow()
//            val imageCompressor = ImageCompressor(activity)
            val file = mFile


            val credentials =
                BasicAWSCredentials(activity!!.resources.getString(R.string.access_key), activity!!.resources.getString(R.string.secreat_key))
            val s3Client = AmazonS3Client(credentials)

            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1))
            val transferUtility = TransferUtility.builder()
                .context(activity)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .s3Client(s3Client)
                .build()

            val uploadObserver =
                transferUtility.upload(
                    AppUtil.s3BucketProductName,
                    fileName,
                    file,
                    CannedAccessControlList.PublicRead
                )


            uploadObserver.setTransferListener(object : TransferListener {

                override fun onStateChanged(id: Int, state: TransferState) {
                    Log.e("state", state.name)
                    if (TransferState.COMPLETED == state) {
                        val profilePath =
                            "https://s3.ap-south-1.amazonaws.com/gadgetzone/user_product_images/$fileName"
                        bucketApiListener.onPicUploadedOnBucket(profilePath)
                        objProgressDialog?.dialogDismiss()

                    } else if (state.name.equals("FAILED", ignoreCase = true)) {
                        bucketApiListener.onPicNotUploadedOnBucket("File uploading failed! Please try again")
                        objProgressDialog?.dialogDismiss()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val percent = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                    val percentDone = percent.toInt()
                    Log.e("fileUpload", "" + percentDone)
//                    progress_bar?.progress = percentDone
//                    progress_percentage_text?.text = "$percentDone/100"
                }

                override fun onError(id: Int, ex: Exception) {
                    //  pd.dismiss();
                    objProgressDialog?.dialogDismiss()
                    bucketApiListener.onPicNotUploadedOnBucket(ex.message)
                }
            })

        }
    }


    interface AwsBucketApiListener {
        fun onPicUploadedOnBucket(imagePath: String?)
        fun onPicNotUploadedOnBucket(message: String?)
        fun onPicUpdationProgress(percent : Int?)
    }
}