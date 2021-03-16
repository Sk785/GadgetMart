package com.gadgetmart.ui.edit_profile

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.gadgetmart.R
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.registeration.RegistrationRequest
import com.gadgetmart.util.custom.CustomProgressDialog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfilePresenter(val context: Activity, contract: EditProfileContract) :
    BasePresenter<EditProfileContract>(contract, null) {

    /**
     * Validates the data received. If valid, makes API call
     */
    private lateinit var myDialog: CustomProgressDialog

    fun validateFields(authToken: String?, registrationRequest: RegistrationRequest,isUpload:Boolean) {

        if (!hasContract()) {
            return
        }
        if (TextUtils.isEmpty(registrationRequest.getUserName())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (TextUtils.isEmpty(registrationRequest.getEmailId())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        myDialog = CustomProgressDialog(context)
        authToken?.let {
            editUserProfile(it, registrationRequest,isUpload)
        }

    }

    /**
     * Makes API request call and informs the view when response is received
     */
    private fun editUserProfile(authToken: String, registrationRequest: RegistrationRequest,isUpload:Boolean) {
        var body: MultipartBody.Part?=null
        Log.e("registrationRequest",registrationRequest.getProfilePhoto());
        if(isUpload){
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

//        showProgressDialog();
        myDialog.dialogShow()
        ApiClientGenerator
            .getClient()!!
            .editProfile(authToken,body,countryCode,phone,email,name,gender)
            .enqueue(object : Callback<ApiResponse<AuthResult>> {

                override fun onResponse(
                    call: Call<ApiResponse<AuthResult>>,
                    response: Response<ApiResponse<AuthResult>>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProfileUpdationFailed(response.message())
                        return
                    }
                    if(response.body()!!.status==1){
                        getContract().onProfileUpdated(response.body()!!.data, response.message())

                    }else{
                        getContract().onProfileUpdationFailed(response.body()!!.message)

                    }
//                    PreferenceManager().getInstance(context).saveSignInResult(response.body()!!.data)
                }

                override fun onFailure(
                    call: Call<ApiResponse<AuthResult>>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    getContract().onProfileUpdationFailed(t.message!!)
                }
            })
    }
}