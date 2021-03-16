package com.gadgetmart.ui.products

import com.gadgetmart.ui.category.Offers
import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName ("variations") val data : ArrayList<ProductVariation>?,
    @SerializedName ("id") val id : Int?,
    @SerializedName ("name") val name : String?,
    @SerializedName ("sort_order") val sort_order : Int?,
    @SerializedName ("enabled") val enabled : Boolean?,
    @SerializedName ("created_at") val created_at : String?,
    @SerializedName ("updated_at") val updated_at : String?,
    @SerializedName ("offers") val offers : ArrayList<Offers>?






    ):Pagination()