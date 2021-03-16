package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductReviewModel (
    @SerializedName("id") val variationId: String,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("title") val title: String?,


    @SerializedName("description") val description: String?,
    @SerializedName("rating") val rating: Int?


    ) : Parcelable