package com.gadgetmart.ui.products

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName

data class PopularSectionProductData(
    @SerializedName("products") val products: ArrayList<ProductVariation>?,
    @SerializedName("cart_count") val cartCount: String?
)