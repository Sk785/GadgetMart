package com.gadgetmart.ui.coupon

import android.content.Context
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.auth._common.AuthResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponPresenter(val context: Context, contract: CouponContract) :
    BasePresenter<CouponContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    fun fetchUserProfile(authToken: String) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onReviewNotFound("No network connectivity")
        }

//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .fetchUserProfile(authToken)
            .enqueue(object : Callback<ApiResponse<AuthResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<AuthResult>?>,
                    response: Response<ApiResponse<AuthResult>?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onReviewNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onReviewFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onReviewNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AuthResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onReviewNotFound(t.message!!)
                }
            })
    }
}