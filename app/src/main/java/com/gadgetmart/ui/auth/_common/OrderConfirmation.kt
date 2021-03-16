package com.gadgetmart.ui.auth._common

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class OrderConfirmation(

    @SerializedName("id") val id : String,

    @SerializedName("order_number") val order_number : String,
    @SerializedName("user_id") val user_id : String,
    @SerializedName("payment_type") val payment_type : String,
    @SerializedName("payment_status") val payment_status : String,
    @SerializedName("delivery_address") val delivery_address : DeliveryAddress,
    @SerializedName("payment_config") val payment_config : PaymentConfig

    )