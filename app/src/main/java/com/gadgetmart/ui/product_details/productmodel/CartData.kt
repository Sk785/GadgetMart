package com.gadgetmart.ui.product_details.productmodel

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CartData(
    @SerializedName("data")  val data: DataM?,
    @SerializedName("message") val message: String?,
    @SerializedName("status")val status: Int?
)