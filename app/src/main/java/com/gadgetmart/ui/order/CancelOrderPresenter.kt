package com.gadgetmart.ui.order

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.gadgetmart.base.BasePresenter
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.util.ContextUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


public class CancelOrderPresenter(val context: Activity, contract: CancelOrderContract ) : BasePresenter<CancelOrderContract>(contract,null){


    fun completeCancelOrder(order_id : String,reason : String ,product_image:ArrayList<String>?) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().noNetworkFound("No network connectivity")
        }
        var parts: ArrayList<MultipartBody.Part>?=ArrayList();

        for (i in 0 until product_image!!.size) {
            parts!!.add(prepareFilePart(product_image.get(i)))
        }

        val reason: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            reason
        )

        val order_id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            order_id
        )

//        showProgressDialog();s
        try {

            ApiClientGenerator
                .getClient()!!
                .cancelCompleteOrder(parts,reason,order_id,ContextUtils.getAuthToken(context))
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("copupon response", response.toString())
                        if (response.getString("status") == "1") {
                            getContract().onCancelOrderSuccess(response.getString("message").toString())
                        } else {
                            getContract().onCancelOrderFailure(response.getString("message").toString())
                        }

                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        getContract().onCancelOrderFailure("Not found")

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun prepareFilePart(fileUri: String): MultipartBody.Part {
        var file=File(fileUri)
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)

       var  body =
            MultipartBody.Part.createFormData("product_image[]", file.name, requestFile)
        return body
    }
    fun cancelOrder(productid:String,order_id : String,reason : String ,product_image:ArrayList<String>?) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().noNetworkFound("No network connectivity")
        }
        var parts: ArrayList<MultipartBody.Part>?=ArrayList();

        for (i in 0 until product_image!!.size) {
            parts!!.add(prepareFilePart(product_image.get(i)))
        }

        val reason: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            reason
        )

        val order_id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            order_id
        )

        val productid: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            productid
        )

//        showProgressDialog();
        try {

            ApiClientGenerator
                .getClient()!!
                .cancelOrder(order_id,productid,reason,parts,ContextUtils.getAuthToken(context))
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {

                          if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("copupon response", response.toString())
                        if (response.getString("status") == "1") {
                           getContract().onCancelOrderSuccess(response.getString("message").toString())
                        } else {
                            getContract().onCancelOrderFailure(response.getString("message").toString())
                        }

                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        getContract().onCancelOrderFailure("Not found")

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun replaceRefund(productid:String,order_id : String,reason : String,requestType:String,product_image:ArrayList<String>?) {

        if (!hasContract()) {
            return
        }

        if (!hasNetwork()) {
            getContract().noNetworkFound("No network connectivity")
        }
        var parts: ArrayList<MultipartBody.Part>?=ArrayList();

        for (i in 0 until product_image!!.size) {
            parts!!.add(prepareFilePart(product_image.get(i)))
        }

        val reason: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            reason
        )

        val order_id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            order_id
        )

        val productid: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            productid
        )

        val requestType: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            requestType
        )
//        showProgressDialog();
        try {

            ApiClientGenerator
                .getClient()!!
                .replaceAndRetrunOrder(order_id,productid,requestType,reason,parts,ContextUtils.getAuthToken(context))
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("copupon response", response.toString())
                        if (response.getString("status") == "1") {
                            getContract().onCancelOrderSuccess(response.getString("message").toString())
                        } else {
                            getContract().onCancelOrderFailure(response.getString("message").toString())
                        }

                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        getContract().onCancelOrderFailure("Not found")

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
