package com.gadgetmart.ui.products

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoveFromWishListPresenter(val context: Activity, contract: RemoveFromWishListContract) :
    BasePresenter<RemoveFromWishListContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog

    fun removeFromWishList(wishListId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onNotRemovedFromWishList("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .removeFromWishList(authToken, wishListId)
            .enqueue(object : Callback<ApiResponse<WishListRemovedResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    response: Response<ApiResponse<WishListRemovedResult>?>
                ) {
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onNotRemovedFromWishList(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onRemovedFromWishList(
                            response.body()!!.message
                        )
                    } else {
                        getContract().onNotRemovedFromWishList(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    t: Throwable
                ) {
                    getContract().onNotRemovedFromWishList(t.message!!)
                }
            })
    }
}