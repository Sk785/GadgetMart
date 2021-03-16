package com.gadgetmart.ui.my_review

import com.gadgetmart.ui.products.Pagination
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.google.gson.annotations.SerializedName

data class ReviewData (
    @SerializedName("data") var data : ArrayList<ReviewList>?


): Pagination()