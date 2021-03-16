package com.gadgetmart.ui.wishlist

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListPresenter(val context: Activity, contract: WishListContract) :
    BasePresenter<WishListContract>(contract, null) {

    private lateinit var myDialog: CustomProgressDialog
    fun getMyWishList(authToken: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onWisListDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getMyWishList(authToken)
            .enqueue(object : Callback<ApiResponse<WishListRemovedResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    response: Response<ApiResponse<WishListRemovedResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onWisListDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onWishListDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetWishListFlag,
                            response.body()!!.status
                        )
                    } else {
                        getContract().onWisListDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onWisListDataNotFound(t.message!!)
                }
            })
    }

    fun removeFromWishList(wishListId: Int, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onWisListDataNotFound("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .removeFromWishList(authToken, ""+wishListId)
            .enqueue(object : Callback<ApiResponse<WishListRemovedResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    response: Response<ApiResponse<WishListRemovedResult>?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onWisListDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onWishListDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.RemoveWishListFlag,
                            response.body()!!.status
                        )
                    } else {
                        getContract().onWisListDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onWisListDataNotFound(t.message!!)
                }
            })
    }
}