package com.gadgetmart.ui.cart_bag.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class Data(
    @SerializedName("my_cart") val my_cart: ArrayList<MyCart>?
)