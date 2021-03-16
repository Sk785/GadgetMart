package com.gadgetmart.ui.search

import android.app.Activity
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.products.AddToWishListContract
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenter (val context: Activity, contract: SearchContract) :
    BasePresenter<SearchContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog

    fun addToSearchList(page:Int,keyword: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onNotAddedToSearchList("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .searchProducts(page,authToken, keyword)
            .enqueue(object : Callback<ApiResponse<SearchProduct>?> {

                override fun onResponse(
                    call: Call<ApiResponse<SearchProduct>?>,
                    response: Response<ApiResponse<SearchProduct>?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onNotAddedToSearchList(response.message())
                        return
                    }
//                    val gson = Gson()
//                    val json = gson.toJson(response.body()!!.data)
//                    Log.e("resonse",json)
                    if (response.body() != null) {
                        if(response.body()!!.status==1){
                            getContract().onAddedToSearchList(
                                    response.body()!!.data
                            )
                        }else{
                            getContract().onNotAddedToSearchList(
                                    response.message()
                            )
                        }

                    } else {
                        AppUtil.firebaseEvent(context,"error","error_events",                            response.message()
                        )

                        getContract().onNotAddedToSearchList(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<SearchProduct>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onNotAddedToSearchList(t.message!!)
                }
            })
    }
}