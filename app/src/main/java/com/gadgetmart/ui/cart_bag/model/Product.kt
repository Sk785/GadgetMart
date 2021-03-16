package com.gadgetmart.ui.cart_bag.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("category_id") val category_id: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("created_by") val created_by: String?,
    @SerializedName("current_batch") val current_batch: CurrentBatch?,
    @SerializedName("deleted_by") val deleted_by: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("enabled") val enabled: String?,
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("brand") val brand: String?,


    @SerializedName("image") val image: String?,
    @SerializedName("manage_stock") val manage_stock: String?,
    @SerializedName("merchant_id") val merchant_id: Int? = 0,
    @SerializedName("min_order_quantity") val min_order_quantity: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("popular") val popular: String?,
    @SerializedName("regular_price") val regular_price: String?,
    @SerializedName("related_files") val related_files: String?,
    @SerializedName("related_products") val related_products: String?,
    @SerializedName("sale_end") val sale_end: String?,
    @SerializedName("sale_price") val sale_price: String?,
    @SerializedName("sale_start") val sale_start: String?,
    @SerializedName("short_description") val short_description: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("sort_order") val sort_order: Int? = 0,
    @SerializedName("stock_quantity") val stock_quantity: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("tax_id") val tax_id: Int? = 0,
    @SerializedName("taxable") val taxable: String?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("updated_by") val updated_by: String?,
    @SerializedName("weight") val weight: Int? = 0,
    @SerializedName("tax") val tax: Tax?
) : Parcelable