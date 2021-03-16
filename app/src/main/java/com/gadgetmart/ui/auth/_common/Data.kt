package com.gadgetmart.ui.auth._common

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
 class Data (

   @SerializedName("order_number") val order_number : String,
   @SerializedName("user_id") val user_id : Int,
   @SerializedName("payment_type") val payment_type : String,
   @SerializedName("payment_status") val payment_status : String,
   @SerializedName("bag_amount") val bag_amount : Double,
   @SerializedName("tax") val tax : Double,
   @SerializedName("coupon_discount") val coupon_discount : Int,
   @SerializedName("total_amount") val total_amount : Double,
   @SerializedName("updated_at") val updated_at : String,
   @SerializedName("created_at") val created_at : String,
   @SerializedName("id") val id : Int,
   @SerializedName("payment_config") val payment_config : PaymentConfig,
   @SerializedName("delivery_address") val delivery_address : DeliveryAddress,
   @SerializedName("payment_response") val payment_response : String
)
