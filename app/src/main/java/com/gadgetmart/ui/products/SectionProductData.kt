package com.gadgetmart.ui.products

import com.google.gson.annotations.SerializedName

data class SectionProductData(
    @SerializedName("products") val data: ProductData?,
    @SerializedName("cart_count") val cartCount: String?,
    @SerializedName("current_page") val current_page: String?

)