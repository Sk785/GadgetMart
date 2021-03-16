package com.gadgetmart.ui.dashboard

import com.gadgetmart.ui.category.CategoryItem

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class DashboardResult(
    @SerializedName("cart_count") val cartCount: String?,
    @SerializedName("categories") val categories: ArrayList<CategoryItem>?,
    @SerializedName("sections") val sections: ArrayList<DashboardSections>?
)