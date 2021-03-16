package com.gadgetmart.ui.order.model

import android.os.Parcelable
import com.gadgetmart.ui.my_address.AddAddressItems
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelArray (
    @SerializedName("id") val id: String?,
    @SerializedName("order_number") val order_number: String?,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("payment_type") val payment_type: String?,
    @SerializedName("payment_status") val payment_status: String?,
    @SerializedName("transaction_no") val transaction_no: String?,
    @SerializedName("bag_amount") val bag_amount: String?,
    @SerializedName("tax") val tax: String?,
    @SerializedName("discount") val discount: String?,


    @SerializedName("coupon_code") val coupon_code: String?,
    @SerializedName("coupon_discount") val coupon_discount: String?,
    @SerializedName("delivery_charges") val delivery_charges: String?,
    @SerializedName("total_amount") val total_amount: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("delivery_address") val delivery_address: AddAddressItems?,
    @SerializedName("order_products") val order_products: ArrayList<OrderProducts>?,
        @SerializedName("invoice_url") val invoice_url: String?,
    @SerializedName("is_order_cancel") val is_order_cancel: Boolean?





    ): Parcelable