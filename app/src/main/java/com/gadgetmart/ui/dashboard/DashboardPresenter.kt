package com.gadgetmart.ui.dashboard

import android.app.Activity
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardPresenter(val context: Activity, contract: DashboardContract) :
    BasePresenter<DashboardContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private var myDialog: CustomProgressDialog = CustomProgressDialog(context)
    fun getDashboardData(authToken: String, isDialogShows: Boolean) {
        Log.e("token",authToken)

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onDashboardDataNotFound("No network connectivity")
        }

//        showProgressDialog();
        if (isDialogShows) {
            myDialog.dialogShow()
        }
        ApiClientGenerator
            .getClient()!!
            .fetchDashboardData(authToken)
            .enqueue(object : Callback<ApiResponse<DashboardResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<DashboardResult>?>,
                    response: Response<ApiResponse<DashboardResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onDashboardDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onDashboardDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onDashboardDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<DashboardResult>?>,
                    t: Throwable
                ) {
                    Log.e("exception",t.message!!);

//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onDashboardDataNotFound(t.message!!)
                }
            })
    }
}