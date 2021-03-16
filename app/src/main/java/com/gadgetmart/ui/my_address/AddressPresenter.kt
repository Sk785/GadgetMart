package com.gadgetmart.ui.my_address

import android.app.Activity
import android.text.TextUtils
import com.gadgetmart.R
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.AppValidationUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressPresenter(val context: Activity, contract: MyAddressContract) :
    BasePresenter<MyAddressContract>(contract, null) {


    private lateinit var myDialog: CustomProgressDialog

    fun validateFields(addressRequest: AddressRequest, authToken: String) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(addressRequest.getName())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.validateLetters(addressRequest.getName())) {
            getContract().showToast("Invalid name")
            return
        }

        if (TextUtils.isEmpty(addressRequest.getAddress1())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getAddress2())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getCity())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.validateLetters(addressRequest.getCity())) {
            getContract().showToast("Invalid city")
            return
        }
        if (TextUtils.isEmpty(addressRequest.getState())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getCountry())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getZip())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

//        if (!AppValidationUtil.validateLetters(addressRequest.getAddress1())) {
//            getContract().showToast("Invalid house number / building number")
//            return
//        }

        if (TextUtils.isEmpty(addressRequest.getAddress2())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getPhoneNumber())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.isValidPhoneNumber(addressRequest.getPhoneNumber())) {
            getContract().showToast("Phone Number is Invalid.")
            return
        }

//        if (TextUtils.isEmpty(addressRequest.getLandmark())
//        ) {
//            getContract().showToast(R.string.error_msg_empty)
//            return
//        }
        AppUtil.firebaseEventForAddress(context,"state","city","address",addressRequest.getState()!!,addressRequest.getCity()!!)

        myDialog = CustomProgressDialog(context)
        addAddress(addressRequest, authToken)

    }

    fun addAddress(addressRequest: AddressRequest, authToken: String) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onAddressDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .saveMyAddress(addressRequest, authToken)
            .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AddressResult>?>,
                    response: Response<ApiResponse<AddressResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onAddressDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddressDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                            , AppUtil.AddAddressFlag
                        )
                    } else {
                        getContract().onAddressDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AddressResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onAddressDataNotFound(t.message!!)
                }
            })
    }

    fun updateFields(addressRequest: AddressRequest, authToken: String) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(addressRequest.getName())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.validateLetters(addressRequest.getName())) {
            getContract().showToast("Invalid name")
            return
        }

        if (TextUtils.isEmpty(addressRequest.getAddress1())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getAddress2())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getCity())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getState())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getCountry())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getZip())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.validateLetters(addressRequest.getCity())) {
            getContract().showToast("Invalid city")
            return
        }

//        if (!AppValidationUtil.validateAddress(addressRequest.getAddress1())) {
//            getContract().showToast("Invalid house number / building number")
//            return
//        }

        if (TextUtils.isEmpty(addressRequest.getAddress2())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }
        if (TextUtils.isEmpty(addressRequest.getPhoneNumber())) {
            getContract().showToast(R.string.error_msg_empty)
            return
        }

        if (!AppValidationUtil.isValidPhoneNumber(addressRequest.getPhoneNumber())) {
            getContract().showToast("Phone Number is Invalid.")
            return
        }

//        if (TextUtils.isEmpty(addressRequest.getLandmark())
//        ) {
//            getContract().showToast(R.string.error_msg_empty)
//            return
//        }

        editAddress(addressRequest, authToken)

    }

    fun editAddress(addressRequest: AddressRequest, authToken: String) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onAddressDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .updateMyAddress(addressRequest, authToken)
            .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AddressResult>?>,
                    response: Response<ApiResponse<AddressResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onAddressDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddressDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                            , AppUtil.EditAddressFlag
                        )
                    } else {
                        getContract().onAddressDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AddressResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onAddressDataNotFound(t.message!!)
                }
            })
    }

    fun getAddresses(authToken: String, isDialogShows: Boolean) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onAddressDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getAddresses(authToken)
            .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AddressResult>?>,
                    response: Response<ApiResponse<AddressResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onAddressDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddressDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetAddressesFlag
                        )
                    } else {
                        getContract().onAddressDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AddressResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onAddressDataNotFound(t.message!!)
                }
            })
    }

    fun removeAddresses(addressId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onAddressDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .removeMyAddress(addressId, authToken)
            .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AddressResult>?>,
                    response: Response<ApiResponse<AddressResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onAddressDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddressDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.RemoveAddressFlag
                        )
                    } else {
                        getContract().onAddressDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AddressResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onAddressDataNotFound(t.message!!)
                }
            })
    }

    fun setAddressAsDefault(addressId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onAddressNotSetAsDefault("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .setAddressAsDefault(addressId, authToken)
            .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AddressResult>?>,
                    response: Response<ApiResponse<AddressResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onAddressNotSetAsDefault(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddressSetAsDefault(
                            response.body()!!.message
                        )
                    } else {
                        getContract().onAddressNotSetAsDefault(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AddressResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onAddressNotSetAsDefault(t.message!!)
                }
            })
    }
}