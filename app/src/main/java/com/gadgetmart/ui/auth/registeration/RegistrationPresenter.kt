package com.gadgetmart.ui.auth.registeration

import android.app.Activity
import android.text.TextUtils
import com.gadgetmart.R
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.util.AppValidationUtil
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RegistrationPresenter(val context: Activity, contract: RegistrationContract) :
    BasePresenter<RegistrationContract>(contract, null) {

    /**
     * Validates the data received. If valid, makes API call
     */
    private lateinit var myDialog: CustomProgressDialog

    fun validateFields(registrationRequest: RegistrationRequest) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(registrationRequest.getCountryCode())) {
            getContract().showToast(R.string.error_msg_phone_number)
            return
        }

        if (!AppValidationUtil.isValidPhoneNumber(registrationRequest.getPhoneNumber())) {
            getContract().showToast(R.string.error_msg_phone_number)
            return
        }
//        if (!AppValidationUtil.isProfileImage(registrationRequest.getProfilePhoto())) {
//            getContract().showToast(R.string.error_msg_person_image)
//            return
//        }

        if (TextUtils.isEmpty(registrationRequest.getDeviceToken())) {
            getContract().showToast("Invalid device token")
            return
        }

        myDialog = CustomProgressDialog(context)
        getSignedIn(registrationRequest)

    }

    /**
     * Makes API request call and informs the view when response is received
     */
    private fun getSignedIn(registrationRequest: RegistrationRequest) {

//        var file: File? = null
//        var filePart: MultipartBody.Part?=null
//        if (TextUtils.isEmpty(registrationRequest.getProfilePhoto())) {
//
//        } else {
        var body:MultipartBody.Part?=null
        if(registrationRequest.getProfilePhoto()!=null){
            val file = File(registrationRequest.getProfilePhoto())
            val requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)

             body =
                MultipartBody.Part.createFormData("photo", file.name, requestFile)

        }
//           val filePart = MultipartBody.Part.createFormData(
//                "photo",
//                file!!.path,
//                RequestBody.create(MediaType.parse("photo"), file)
//            )

//        val photo = MultipartBody.Part.createFormData(
//            "file",
//            file.name,
//            RequestBody.create(MediaType.parse("photo/*"), file)
//        )


        // }


        val countryCode: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getCountryCode()

        )

        val phone: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getPhoneNumber()
        )
        val name: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getUserName()
        )


        val email: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getEmailId()
        )

        val gender: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getGender()
        )
        val device_token: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            registrationRequest.getDeviceToken()
        )

//        showProgressDialog();

        myDialog.dialogShow()
//        if (file != null) {
            ApiClientGenerator
                .getClient()!!
                .registerUser(body, countryCode, phone, email, name, gender, device_token)
                .enqueue(object : Callback<ApiResponse<AuthResult>> {

                    override fun onResponse(
                        call: Call<ApiResponse<AuthResult>>,
                        response: Response<ApiResponse<AuthResult>>
                    ) {
//                    dismissDialog()
                        myDialog.dialogDismiss()
                        if (response.code() < 200 || response.code() >= 300) {
                            if (response.code() == 401) {
                                PreferenceManager().getInstance(context).clearUserData()
                                getContract().onUserRegistrationFailed(response.message())
                                return
                            }
                            getContract().onUserRegistrationFailed(response.message())
                            return
                        }
                        PreferenceManager().getInstance(context)
                            .saveSignInResult(response.body()!!.data)
                        getContract().onUserRegistered(
                            response.body()!!.data,
                            response.body()!!.message,
                            response.body()!!.status
                        )
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<AuthResult>>,
                        t: Throwable
                    ) {
                        myDialog.dialogDismiss()
//                    dismissDialog()
                        getContract().onUserRegistrationFailed(t.message!!)
                    }
                })
//        }else{
//            ApiClientGenerator
//                .getClient()!!
//                .registerWithOutImageUser(countryCode, phone, email, name, gender, device_token)
//                .enqueue(object : Callback<ApiResponse<AuthResult>> {
//
//                    override fun onResponse(
//                        call: Call<ApiResponse<AuthResult>>,
//                        response: Response<ApiResponse<AuthResult>>
//                    ) {
////                    dismissDialog()
//                        myDialog.dialogDismiss()
//                        if (response.code() < 200 || response.code() >= 300) {
//                            if (response.code() == 401) {
//                                PreferenceManager().getInstance(context).clearUserData()
//                                getContract().onUserRegistrationFailed(response.message())
//                                return
//                            }
//                            getContract().onUserRegistrationFailed(response.message())
//                            return
//                        }
//                        PreferenceManager().getInstance(context)
//                            .saveSignInResult(response.body()!!.data)
//                        getContract().onUserRegistered(
//                            response.body()!!.data,
//                            response.message(),
//                            response.body()!!.status
//                        )
//                    }
//
//                    override fun onFailure(
//                        call: Call<ApiResponse<AuthResult>>,
//                        t: Throwable
//                    ) {
//                        myDialog.dialogDismiss()
////                    dismissDialog()
//                        getContract().onUserRegistrationFailed(t.message!!)
//                    }
//                })
//        }
        }

}