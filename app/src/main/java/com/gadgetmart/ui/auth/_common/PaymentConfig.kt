package com.gadgetmart.ui.auth._common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.parceler.Parcel

@Parcel
data class PaymentConfig(

    @SerializedName("txnid") val txnid: String,
    @SerializedName("productInfo") val productInfo: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("email") val email: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("udf5") val udf5: String,
    @SerializedName("hash") val hash: String,
    @SerializedName("currancy") val currancy: String,

    @SerializedName("payment_order_id") val payment_order_id: String,
    @SerializedName("phone") val phone: String



)



