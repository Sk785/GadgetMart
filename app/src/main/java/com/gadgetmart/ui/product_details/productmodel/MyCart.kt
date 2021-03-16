package com.gadgetmart.ui.product_details.productmodel

import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class MyCart(
    @SerializedName("created_at")  val created_at: String?,
    @SerializedName("id")  val id: Int?,
    @SerializedName("product") val product: ProductsDataItems?,
    @SerializedName("product_id") val product_id: Int?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("user_id") val user_id: Int?
)