package com.gadgetmart.ui.checkout.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CheckOutDataModel(

    @SerializedName("address_id") val address_id: String,
    @SerializedName("payment_type") val payment_type: String,
    @SerializedName("platform_type") val platform_type: String,
    @SerializedName("bag_amount") val bag_amount: String,
    @SerializedName("discount") val discount: String,
    @SerializedName("coupon_code") val coupon_code: String,
    @SerializedName("coupon_discount") val coupon_discount: String,
    @SerializedName("delivery_charges") val delivery_charges: String,
    @SerializedName("total_amount") val total_amount: String,
    @SerializedName("gst_no") val gst_no: String="",

    @SerializedName("gst_name") val gst_name: String="",

    @SerializedName("products") val products: ArrayList<ProductModel>

)