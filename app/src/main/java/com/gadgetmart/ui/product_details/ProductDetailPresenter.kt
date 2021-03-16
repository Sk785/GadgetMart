package com.gadgetmart.ui.product_details

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.custom.CustomProgressDialog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailPresenter(val context: Activity, contract: ProductDetailContact) :
    BasePresenter<ProductDetailContact>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog
    fun getProductDetail(productId: String, authToken: String) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductDetailDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        myDialog.dialogShow()
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getProductDetail(productId, authToken)
            .enqueue(object : Callback<ApiResponse<ProductDetailResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductDetailResult>?>,
                    response: Response<ApiResponse<ProductDetailResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductDetailDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        if (response.body()!!.data.product != null) {
                            getContract().onProductDetailDataFound(
                                response.body()!!.data,
                                response.body()!!.message
                            )
                        } else {
                            getContract().onProductDetailDataNotFound(
                                response.message()
                            )
                        }
                    } else {
                        getContract().onProductDetailDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductDetailResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                    getContract().onProductDetailDataNotFound(t.message!!)
                }
            })
    }

    fun addToWishList(variationId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductDetailDataNotFound("No network connectivity")
        }

        myDialog.dialogShow()
        ApiClientGenerator
            .getClient()!!
            .addToWishList(authToken, variationId)
            .enqueue(object : Callback<ApiResponse<WishListResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListResult>?>,
                    response: Response<ApiResponse<WishListResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductDetailDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductAddedToWishList(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductDetailDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    getContract().onProductDetailDataNotFound(t.message!!)
                }
            })
    }

    fun removeFromWishList(wishListId: Int, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductDetailDataNotFound("No network connectivity")
        }

        myDialog.dialogShow()
        ApiClientGenerator
            .getClient()!!
            .removeFromWishList(authToken, ""+wishListId)
            .enqueue(object : Callback<ApiResponse<WishListRemovedResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    response: Response<ApiResponse<WishListRemovedResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductDetailDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductRemovedFromWishList(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductDetailDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    getContract().onProductDetailDataNotFound(t.message!!)
                }
            })
    }


    fun addToRecent(variationId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductRecentNotAdded("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .addToRecentViewedProduct(authToken, ""+variationId)
            .enqueue(object : Callback<ResponseBody?> {

                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductRecentNotAdded(response.message())
                        return
                    }
                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body()?.string()!!)
                        val jsonObjectData = jsonObject.getString("message")
                        getContract().productAddToRecent(
                            jsonObjectData
                        )
                    } else {
                        getContract().onProductRecentNotAdded(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ResponseBody?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onProductRecentNotAdded(t.message!!)
                }
            })
    }

}