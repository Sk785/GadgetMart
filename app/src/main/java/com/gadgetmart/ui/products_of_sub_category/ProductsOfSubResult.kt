package com.gadgetmart.ui.products_of_sub_category

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class ProductsOfSubResult (
    @SerializedName ("products") val products : ProductsDetailItems?,
    @SerializedName("cart_count") val cartCount: String?,
    @SerializedName ("filterOptions") val filterOptions : ArrayList<ProductsFilterOptions>?
)
