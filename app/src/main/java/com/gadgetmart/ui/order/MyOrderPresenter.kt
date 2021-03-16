package com.gadgetmart.ui.order

import android.app.Activity
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.order.model.MyOrderResult
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderPresenter(val context: Activity, contract: MyOrderContract) :
    BasePresenter<MyOrderContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private var myDialog: CustomProgressDialog = CustomProgressDialog(context)
    fun getDashboardData(pageno: Int, authToken: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onMyOrderDataNotFound("No network connectivity")
        }

//        showProgressDialog();
        if (isDialogShows) {
            myDialog.dialogShow()
        }
        ApiClientGenerator
            .getClient()!!
            .getMyOrders(pageno, authToken)
            .enqueue(object : Callback<MyOrderResult?> {

                override fun onResponse(
                    call: Call<MyOrderResult?>,
                    response: Response<MyOrderResult?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onMyOrderDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {

                        response.body()!!.message?.let {
                            getContract().onMyOrderDataFound(
                                response.body()!!.data,
                                it,
                                AppUtil.GetRegularProductsFlag
                            )
                        }
                    } else {
                        getContract().onMyOrderDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<MyOrderResult?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    Log.e("error", t.message)
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onMyOrderDataNotFound(t.message!!)
                }
            })
    }


}