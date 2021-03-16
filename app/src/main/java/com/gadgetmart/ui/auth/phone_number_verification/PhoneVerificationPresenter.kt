package com.gadgetmart.ui.auth.phone_number_verification

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneVerificationPresenter(val context: Activity, contract: PhoneVerificationContract) :
    BasePresenter<PhoneVerificationContract>(contract, null) {

    /**
     * Validates the data received. If valid, makes API call
     */

    private lateinit var myDialog: CustomProgressDialog

    fun validateFields(phoneVerificationRequest: PhoneVerificationRequest) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(phoneVerificationRequest.getCountryCode())) {
            getContract().showToast(R.string.error_msg_country_code)
            return
        }

        if (!AppValidationUtil.isValidPhoneNumber(phoneVerificationRequest.getPhoneNumber())) {
            getContract().showToast(R.string.error_msg_phone_number)
            return
        }

        if (TextUtils.isEmpty(phoneVerificationRequest.getDeviceToken())) {
            getContract().showToast("Invalid device token")
            return
        }


        myDialog = CustomProgressDialog(context)
        getSignedIn(phoneVerificationRequest)

    }

    /**
     * Makes API request call and informs the view when response is received
     */
    private fun getSignedIn(phoneVerificationRequest: PhoneVerificationRequest) {

        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .verifyPhoneNumber(phoneVerificationRequest)
            .enqueue(object : Callback<ApiResponse<AuthResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AuthResult>?>,
                    response: Response<ApiResponse<AuthResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onPhoneNumberVerificationFailed(response.message())
                        return
                    }
                    if (response.body() != null) {
                        PreferenceManager().getInstance(context)
                            .saveSignInResult(response.body()!!.data)
                        getContract().onPhoneNumberVerified(
                            response.body()!!.data,
                            response.body()!!.status,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onPhoneNumberVerificationFailed(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AuthResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onPhoneNumberVerificationFailed(t.message!!)
                }
            })
    }


}