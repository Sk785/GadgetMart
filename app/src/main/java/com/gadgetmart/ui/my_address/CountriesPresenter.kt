package com.gadgetmart.ui.my_address

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiListResponse
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountriesPresenter(val context: Activity, contract: CountriesContract) :
    BasePresenter<CountriesContract>(contract, null) {


    private lateinit var myDialog: CustomProgressDialog

    fun getCountries(authToken: String, isDialogShows: Boolean) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().showToast("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getCountries(authToken)
            .enqueue(object : Callback<ApiListResponse<Country>?> {

                override fun onResponse(
                    call: Call<ApiListResponse<Country>?>,
                    response: Response<ApiListResponse<Country>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onCountriesNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onCountriesFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetAddressesFlag
                        )
                    } else {
                        getContract().onCountriesNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiListResponse<Country>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onCountriesNotFound(t.message!!)
                }
            })
    }

    fun getStates(authToken: String, countryId: String, isDialogShows: Boolean) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onStatesNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getStates(countryId, authToken)
            .enqueue(object : Callback<ApiResponse<Country>?> {

                override fun onResponse(
                    call: Call<ApiResponse<Country>?>,
                    response: Response<ApiResponse<Country>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onStatesNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onStatesFound(
                            response.body()!!.data.states,
                            response.body()!!.message,
                            AppUtil.GetAddressesFlag
                        )
                    } else {
                        getContract().onStatesNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<Country>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onStatesNotFound(t.message!!)
                }
            })
    }

    fun getCities(authToken: String, stateId: String, isDialogShows: Boolean) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onCitiesNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getCities(stateId, authToken)
            .enqueue(object : Callback<ApiResponse<State>?> {

                override fun onResponse(
                    call: Call<ApiResponse<State>?>,
                    response: Response<ApiResponse<State>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onCitiesNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onCitiesFound(
                            response.body()!!.data.cities,
                            response.body()!!.message,
                            AppUtil.GetAddressesFlag
                        )
                    } else {
                        getContract().onCitiesNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<State>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onCitiesNotFound(t.message!!)
                }
            })
    }
}