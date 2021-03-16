package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductImages(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("product_id") val product_id: Int? = 0,
    @SerializedName("name") val name: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) : Parcelable
