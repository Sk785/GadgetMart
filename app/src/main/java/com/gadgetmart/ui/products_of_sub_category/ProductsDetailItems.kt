package com.gadgetmart.ui.products_of_sub_category

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class ProductsDetailItems (
    @SerializedName ("current_page") val current_page : Int?,
    @SerializedName ("data") val data : ArrayList<ProductVariation>?,
    @SerializedName ("first_page_url") val first_page_url : String?,
    @SerializedName ("from") val from : String?,
    @SerializedName ("last_page") val last_page : Int?,
    @SerializedName ("next_page_url") val next_page_url : String?,
    @SerializedName ("path") val path : String?,
    @SerializedName ("per_page") val per_page : Int?,
    @SerializedName ("prev_page_url") val prev_page_url : String?,
    @SerializedName ("to") val to : String?,
    @SerializedName ("total") val total : Int?
)