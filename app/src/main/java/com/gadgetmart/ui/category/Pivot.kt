package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pivot(
    @SerializedName("product_id") val productId: String?,
    @SerializedName("category_id") val categoryId: String?
) : Parcelable