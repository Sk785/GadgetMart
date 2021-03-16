package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PopularGadgetImage(
    @SerializedName("id") val itemImageId: String?,
    @SerializedName("product_id") val productId: String?,
    @SerializedName("sku_id") val skuId: String?,
    @SerializedName("name") val itemImageNameOrUrl: String?
) : Parcelable