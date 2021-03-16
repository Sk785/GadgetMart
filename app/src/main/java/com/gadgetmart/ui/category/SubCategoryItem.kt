package com.gadgetmart.ui.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubCategoryItem(
    @SerializedName("id") val itemId: String?,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("name") val itemName: String?,
    @SerializedName("image") val itemImage: String?,
    @SerializedName("enabled") val enabled: String?,
    @SerializedName("pivot") val pivot: Pivot?,
    @SerializedName("products_count") val products_count: Int? = 0,
    @SerializedName("sku_count") val sku_count: Int? = 0,


    @SerializedName("category") val subCategoryItem: SubCategoryItem?
) : Parcelable