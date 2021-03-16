package com.gadgetmart.ui.products

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName

data class PopularSectionProduct (
    @SerializedName ("data") val data : ArrayList<ProductVariation>?
): Pagination()