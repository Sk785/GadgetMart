package com.gadgetmart.ui.dashboard

import com.gadgetmart.ui.category.Pivot


import android.os.Parcelable
import com.gadgetmart.ui.cart_bag.model.Product
import com.gadgetmart.ui.product_details.CartInfo
import com.gadgetmart.ui.products_of_sub_category.ProductDataWishlist
import com.gadgetmart.ui.products_of_sub_category.ProductImages
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VaridationData(
    @SerializedName("id") val variationId: String,
    @SerializedName("product_id") val productId: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("variation_name") val variationName: String?,
    @SerializedName("variation_value") val variationValue: String?,
    @SerializedName("current_batch_quantity") val currentBatchQuantity: String?,
    @SerializedName("selling_price") val salePrice: Float? = 0f,
    @SerializedName("price_with_tax") val priceWithTax: String?,
    @SerializedName("discount_price_with_tax") val discount_price_with_tax: String?,
    @SerializedName("save_amount") val save_amount: String?,
    @SerializedName("offer_available") val offer_available: Boolean?,
    @SerializedName("offer_discount_price") val offer_discount_price: Float?,
    @SerializedName("balance_quantity") val balanceQuantity: Int? = 0,
    @SerializedName("current_batch") val currentBatch: CurrentBatch?,
    @SerializedName("images") val productImages: ArrayList<ProductImages>?,
    @SerializedName("product_specifications") val productSpecifications: ArrayList<ProductSpecification>?,
    @SerializedName("cart") val cart: CartInfo?,
    @SerializedName("pivot") val pivot: Pivot?,
    var isAddedToWishList: Boolean?,
    @SerializedName("wish_list") var wishList: ProductDataWishlist?,
    @SerializedName("product") val product: Product?,

    var isSelected: Boolean = false
) : Parcelable