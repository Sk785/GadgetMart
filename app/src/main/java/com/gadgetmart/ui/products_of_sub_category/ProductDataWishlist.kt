package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataWishlist(
    @SerializedName("id") val wishList_id: Int? = 0,
    @SerializedName("product_id") val product_id: Int? = 0,
    @SerializedName("sku_id") val variation_id: String? = null,
    @SerializedName("user_id") val user_id: Int? = 0,
    @SerializedName("product") val product: ProductsDataItems?,
    @SerializedName("variation") val variation: ProductVariation?
) : Parcelable
