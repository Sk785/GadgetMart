package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsFilterOptions(
    @SerializedName("name") val name : String?,
    @SerializedName("filter_options") val filter_options : ArrayList<String>?
) : Parcelable
