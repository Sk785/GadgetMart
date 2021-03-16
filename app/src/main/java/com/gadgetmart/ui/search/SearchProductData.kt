package com.gadgetmart.ui.search

import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.products.Pagination
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.google.gson.annotations.SerializedName

data class SearchProductData (
    @SerializedName("data") val data : ArrayList<ProductVariation>?







    ): Pagination()