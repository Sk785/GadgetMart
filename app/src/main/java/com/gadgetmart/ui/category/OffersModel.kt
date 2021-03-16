package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OffersModel (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("discount_option") val discount_option: String?,
    @SerializedName("discount_amount") val discount_amount: Float?,
    @SerializedName("discount_type") val discount_type: String?,
    @SerializedName("min_order_amount") val min_order_amount: Float?,
    @SerializedName("discount_upto") val discount_upto: Float?,



    @SerializedName("terms") val terms: String?



    ) : Parcelable