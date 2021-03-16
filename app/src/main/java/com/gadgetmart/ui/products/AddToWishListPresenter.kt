package com.gadgetmart.ui.products

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToWishListPresenter(val context: Activity, contract: AddToWishListContract) :
    BasePresenter<AddToWishListContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog

    fun addToWishList(variationId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onNotAddedToWishList("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .addToWishList(authToken, variationId)
            .enqueue(object : Callback<ApiResponse<WishListResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListResult>?>,
                    response: Response<ApiResponse<WishListResult>?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onNotAddedToWishList(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onAddedToWishList(
                            response.body()!!.message
                        )
                    } else {
                        getContract().onNotAddedToWishList(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onNotAddedToWishList(t.message!!)
                }
            })
    }
}