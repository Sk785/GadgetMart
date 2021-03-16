package com.gadgetmart.ui.my_review

import android.content.Context
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.util.ContextUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewPresenter(val context: Context, contract: ReviewContract) :
    BasePresenter<ReviewContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    fun reviewList(authToken: String) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onReviewNotFound("No network connectivity")
        }

//        showProgressDialog();
        try {

            ApiClientGenerator
                .getClient()!!
                .myReviews(
                    ContextUtils.getAuthToken(context)
                )
                .enqueue(object : Callback<ApiResponse<ReviewData?>> {
                    override fun onResponse(
                        call: Call<ApiResponse<ReviewData?>>,
                        response: Response<ApiResponse<ReviewData?>>
                    ) {
                        if (response.code() < 200 || response.code() >= 300) {
                            getContract().onReviewNotFound(response.message())
                            return
                        }
                    val gson = Gson()
                    val json = gson.toJson(response.body()!!.data)
                    Log.e("resonse",json)
                        if (response.body() != null) {
                            getContract().onReviewFound(
                                response.body()!!.data!!
                            )
                        } else {
                            getContract().onReviewNotFound(
                                response.message()
                            )
                        }

                    }

                    override fun onFailure(
                        call: Call<ApiResponse<ReviewData?>>,
                        t: Throwable
                    ) {
                        getContract().onReviewNotFound("Not found")

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}