package com.gadgetmart.ui.cart_bag.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CartModel(


    @SerializedName("data") val data: Data?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Int?
)