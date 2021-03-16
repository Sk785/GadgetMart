package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.category.SubCategoryItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsDataItems(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("category_id") val category_id: Int? = 0,
    @SerializedName("merchant_id") val merchant_id: Int? = 0,
    @SerializedName("product_code") val productCode: String? = "",
    @SerializedName("hsn") val hsn: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("brand") val brand: String?,


    @SerializedName("short_description") val short_description: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("manage_stock") val manage_stock: String?,
    @SerializedName("stock_quantity") val stock_quantity: String?,
    @SerializedName("min_stock_quantity") val minStockQuantity: String?,
    @SerializedName("min_order_quantity") val min_order_quantity: String?,
    @SerializedName("weight") val weight: Float? = 0f,
    @SerializedName("unit") val unit: String?,
    @SerializedName("related_files") val related_files: String?,
    @SerializedName("regular_price") val regular_price: Float?,
    @SerializedName("sale_price") val sale_price: Float? = 0f,
    @SerializedName("sale_start") val sale_start: String?,
    @SerializedName("sale_end") val sale_end: String?,
    @SerializedName("taxable") val taxable: String?,
    @SerializedName("tax_id") val tax_id: Int? = 0,
    @SerializedName("image") val image: String?,
    @SerializedName("related_products") val related_products: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("sort_order") val sort_order: String?,
    var isAddedToWishList: Boolean?,
    @SerializedName("categories") val categories: ArrayList<SubCategoryItem>?,
    @SerializedName("variations") val variations: ArrayList<ProductVariation>?,
    @SerializedName("product_specifications") val productSpecifications: ArrayList<ProductSpecification>?,


    @SerializedName("tax") val tax: ProductItemsTax?


) : Parcelable
