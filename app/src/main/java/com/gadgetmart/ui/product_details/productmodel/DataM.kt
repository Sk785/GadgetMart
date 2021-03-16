package com.gadgetmart.ui.product_details.productmodel

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class DataM(
    @SerializedName("my_cart") val my_cart: ArrayList<MyCart>?
)