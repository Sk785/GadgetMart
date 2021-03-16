package com.gadgetmart.ui.cart_bag.model

import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class MyCart(
    @SerializedName("created_at")val created_at: String?,
    @SerializedName("id")val id: Int?,
    @SerializedName("product")val product: Product?,
    @SerializedName("variation")val variation: ProductVariation?,
    @SerializedName("sku_id")val product_id: Int?,
    @SerializedName("quantity")var quantity: Int?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("user_id")val user_id: Int?
)