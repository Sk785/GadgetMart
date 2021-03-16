package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductSubSpecification (
    @SerializedName("id") val id : String?,
    @SerializedName("product_id") val product_id : String?,
    @SerializedName("sku_id") val sku_id : String?,
    @SerializedName("parent_id") val parent_id : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("value") val value : String?,
    @SerializedName("use_in_filters") val use_in_filters : String?,
    @SerializedName("created_at") val created_at : String?,
    @SerializedName("updated_at") val updated_at : String?
    ) : Parcelable
