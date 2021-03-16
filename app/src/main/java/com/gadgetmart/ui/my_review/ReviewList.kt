package com.gadgetmart.ui.my_review

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName

data class ReviewList (
    @SerializedName("id") var id : Int,
    @SerializedName("sku_id") var variation_id : Int,
    @SerializedName("user_id") var user_id : Int,
    @SerializedName("title") var title : String,
    @SerializedName("description") var description : String,
    @SerializedName("rating") var rating : Int,
    @SerializedName("variation") val variation: ProductVariation






    )