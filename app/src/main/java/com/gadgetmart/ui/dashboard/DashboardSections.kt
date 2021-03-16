package com.gadgetmart.ui.dashboard

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class DashboardSections(

    @SerializedName("id") val dealsId : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("app_key") val appKey : String?,
    @SerializedName("banners") val banners : ArrayList<CategoryDeals>?,
    @SerializedName("variations") val variations : ArrayList<ProductVariation?>?
)
