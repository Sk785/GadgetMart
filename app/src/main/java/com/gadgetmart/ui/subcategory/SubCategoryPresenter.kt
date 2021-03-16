package com.gadgetmart.ui.subcategory

import android.app.Activity
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryPresenter(val context: Activity, contract: SubCategoryContract) :
    BasePresenter<SubCategoryContract>(contract, null) {

    /**
     * Makes API request call and informs the view when response is received
     */
    private lateinit var myDialog: CustomProgressDialog
    fun getSubcategories(categoryId: String, authToken: String, isDialogShows: Boolean) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().onSubCategoryDataNotFound("No network connectivity")
        }

        myDialog = CustomProgressDialog(context)
        if (isDialogShows) {
            myDialog.dialogShow()
        }
//        showProgressDialog();
        ApiClientGenerator
            .getClient()!!
            .getSubcategory(categoryId, authToken)
            .enqueue(object : Callback<ApiResponse<SubCategoryResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<SubCategoryResult>?>,
                    response: Response<ApiResponse<SubCategoryResult>?>
                ) {
//                    dismissDialog()
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
                    if (response.code() < 200 || response.code() >= 300) {
                        getContract().onSubCategoryDataNotFound(response.message())
                        return
                    }
                    if (response.body() != null) {
                        getContract().onSubCategoryDataFound(
                            response.body()!!.data,
                            response.body()!!.message
                        )
                    } else {
                        getContract().onSubCategoryDataNotFound(
                            response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<SubCategoryResult>?>,
                    t: Throwable
                ) {
                    if (isDialogShows) {
                        myDialog.dialogDismiss()
                    }
//                    dismissDialog()
                    getContract().onSubCategoryDataNotFound(t.message!!)
                }
            })
    }
}