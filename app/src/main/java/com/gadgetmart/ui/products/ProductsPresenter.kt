package com.gadgetmart.ui.products

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsPresenter(val context: Activity, contract: ProductsContract) :
    BasePresenter<ProductsContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private var myDialog: CustomProgressDialog = CustomProgressDialog(context)

    fun getSectionProducts(pageno: Int, authToken: String, sectionId: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(sectionId)) {
            getContract().onProductsDataNotFound("Invalid section")
        }

        if (!hasNetwork()) {
            getContract().onProductsDataNotFound("No network connectivity")
        }

        if (isDialogShows) {
            myDialog.dialogShow()
        }
        ApiClientGenerator
            .getClient()!!
            .fetchAllSectionProducts(pageno, authToken, sectionId)
            .enqueue(object : Callback<ApiResponse<SectionProductData>?> {

                override fun onResponse(
                    call: Call<ApiResponse<SectionProductData>?>,
                    response: Response<ApiResponse<SectionProductData>?>
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        Log.e("response.body()data",
                                response.body()!!.toString())
                        getContract().onProductsDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductsDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<SectionProductData>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onProductsDataNotFound(t.message!!)
                }
            })
    }

    fun getBannerProducts(pageno: Int, authToken: String, bannerId: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(bannerId)) {
            getContract().onProductsDataNotFound("Invalid banner")
        }

        if (!hasNetwork()) {
            getContract().onProductsDataNotFound("No network connectivity")
        }

        if (isDialogShows) {
            myDialog.dialogShow()
        }
        ApiClientGenerator
            .getClient()!!
            .fetchAllBannerProducts(pageno, authToken, bannerId)
            .enqueue(object : Callback<ApiResponse<SectionProductData>?> {

                override fun onResponse(
                    call: Call<ApiResponse<SectionProductData>?>,
                    response: Response<ApiResponse<SectionProductData>?>
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onProductsDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductsDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<SectionProductData>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onProductsDataNotFound(t.message!!)
                }
            })
    }

    fun getPopularSectionProducts(pageno: Int, authToken: String, sectionId: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (TextUtils.isEmpty(sectionId)) {
            getContract().onProductsDataNotFound("Invalid section")
        }

        if (!hasNetwork()) {
            getContract().onProductsDataNotFound("No network connectivity")
        }

        if (isDialogShows) {
            myDialog.dialogShow()
        }
        Log.e("###################",authToken+" "+sectionId)
        ApiClientGenerator
            .getClient()!!
            .fetchAllSectionProductsNew(pageno, authToken, sectionId)
            .enqueue(object : Callback<ApiResponse<PopularSectionProductData>?> {

                override fun onResponse(
                    call: Call<ApiResponse<PopularSectionProductData>?>,
                    response: Response<ApiResponse<PopularSectionProductData>?>
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onProductsDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        Log.e("response.body()data",
                            response.body()!!.toString())
                        getContract().onPopularProductsDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onProductsDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PopularSectionProductData>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    getContract().onProductsDataNotFound("hello   "+t.message!!)
                }
            })
    }

}