package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Offers (
    @SerializedName("id") val variationId: String,
    @SerializedName("name") val name: String?,
    @SerializedName("discount") val discount: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName("discount_amount") val discount_amount: Int?,
    @SerializedName("discount_type") val discount_type: String?,
    @SerializedName("discount_upto") val discount_upto: Int?





) : Parcelable