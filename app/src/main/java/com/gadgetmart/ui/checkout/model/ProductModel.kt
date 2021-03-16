package com.gadgetmart.ui.checkout.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
class ProductModel (
    @SerializedName("variation_id") val variation_id: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("offer_id") var offer_id: String=""

    )