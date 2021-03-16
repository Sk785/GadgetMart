package com.gadgetmart.ui.products_of_sub_category

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.products.SectionProductData
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsofSubPresenter(val context: Activity, contract: ProductsOfSubContract) :
    BasePresenter<ProductsOfSubContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog

    fun getSectionProducts(pageno: Int, authToken: String, sectionId: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(sectionId)) {
            getContract().onProductsOfSubDataNotFound("Invalid section")
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
        }

        if (isDialogShows) {
            myDialog.dialogShow()
        }
        ApiClientGenerator
            .getClient()!!
            .fetchBannerSectionProducts(pageno, authToken, sectionId)
            .enqueue(object : Callback<ApiResponse<ProductsOfSubResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    response: Response<ApiResponse<ProductsOfSubResult>?>
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        Log.e("response.body()data",
                            response.body()!!.toString())
                        getContract().onProductsOfSubDataFound(
                            response.body()!!.data,
                            response.body()!!.message,8

                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }


    fun getSubcategoriesProducts(
        categoryId: String,
        pageNumber: Int,
        authToken: String,
        sort_type: String,
        filter:JSONArray,
        isDialogShows: Boolean
    ) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        var obj:JSONObject= JSONObject()
        obj.put("sort_type",sort_type)

        obj.put("filter",filter)
        val parser =  JsonParser();
        val json = parser!!.parse(obj.toString()) as JsonObject;


        ApiClientGenerator
            .getClient()!!

            .getProductsOfSubCategory(categoryId, pageNumber, authToken,json)
            .enqueue(object : Callback<ApiResponse<ProductsOfSubResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    response: Response<ApiResponse<ProductsOfSubResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductsOfSubDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetRegularProductsFlag
                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }

    fun addToWishList(variationId: String, authToken: String) {
                if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
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
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductAddedToWishList(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }

    fun removeFromWishList(wishListId: String, authToken: String) {
        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
        }

        ApiClientGenerator
            .getClient()!!
            .removeFromWishList(authToken, wishListId)
            .enqueue(object : Callback<ApiResponse<WishListRemovedResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    response: Response<ApiResponse<WishListRemovedResult>?>
                ) {
//                    dismissDialog()
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductRemovedFromWishList(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<WishListRemovedResult>?>,
                    t: Throwable
                ) {
//                    dismissDialog()
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }

    fun getPopularProductsData(pageNumber: Int, authToken: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getPopularProducts(pageNumber, authToken)
            .enqueue(object : Callback<ApiResponse<ProductsOfSubResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    response: Response<ApiResponse<ProductsOfSubResult>?>
                ) {
//                    dismissDialog
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductsOfSubDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetPopularProductsFlag
                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }

    fun getOfferProductsdata(pageNumber: Int, authToken: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onProductsOfSubDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getOfferProducts(pageNumber, authToken)
            .enqueue(object : Callback<ApiResponse<ProductsOfSubResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    response: Response<ApiResponse<ProductsOfSubResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsOfSubDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductsOfSubDataFound(
                            response.body()!!.data,
                            response.body()!!.message,
                            AppUtil.GetOfferProductsFlag
                        )
                    } else {
                        getContract().onProductsOfSubDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductsOfSubResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onProductsOfSubDataNotFound(t.message!!)
                }
            })
    }

}