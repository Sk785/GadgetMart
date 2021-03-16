package com.gadgetmart.ui.product_details

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VarientModel(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("product_id") val product_id: Int? = 0,
    @SerializedName("sku_id") val sku_id: Int? = 0,

    @SerializedName("parent_id") val parent_id: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("value") val value: String? = "",
    @SerializedName("use_in_filters") val use_in_filters: String? = "",
    @SerializedName("use_in_variations") val use_in_variations: String? = "",
    var isSelectd: Boolean? = false,
    var selectdText: String? = ""



    ) : Parcelable