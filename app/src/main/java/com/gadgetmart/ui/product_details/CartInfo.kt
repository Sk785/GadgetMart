package com.gadgetmart.ui.product_details

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartInfo (
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("user_id") val user_id: Int? = 0,
    @SerializedName("product_id") val product_id: Int? = 0,
    @SerializedName("quantity") val quantity: Int? = 0,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) : Parcelable