package com.gadgetmart.ui.order.model

import com.gadgetmart.ui.cart_bag.model.CurrentBatch
import com.gadgetmart.ui.cart_bag.model.Tax
import com.gadgetmart.ui.product_details.CartInfo
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
 class ProductModel(
    @SerializedName("category_id") val category_id: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("created_by") val created_by: String,
    @SerializedName("current_batch") val current_batch: CurrentBatch,
    @SerializedName("deleted_by") val deleted_by: String,
    @SerializedName("description") val description: String,
    @SerializedName("enabled") val enabled: String,
    @SerializedName("id") val id: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("image") val image: String,
    @SerializedName("manage_stock") val manage_stock: String,
    @SerializedName("merchant_id") val merchant_id: Int,
    @SerializedName("min_order_quantity") val min_order_quantity: String,
    @SerializedName("name") val name: String,
    @SerializedName("popular") val popular: String,
    @SerializedName("regular_price") val regular_price: String,
    @SerializedName("related_files") val related_files: String,
    @SerializedName("related_products") val related_products: String,
    @SerializedName("sale_end") val sale_end: String,
    @SerializedName("sale_price") val sale_price: String,
    @SerializedName("sale_start") val sale_start: String,
    @SerializedName("short_description") val short_description: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("sort_order") val sort_order: Int,
    @SerializedName("stock_quantity") val stock_quantity: String,
    @SerializedName("tags") val tags: String,
    @SerializedName("tax_id") val tax_id: Int,
    @SerializedName("taxable") val taxable: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("updated_by") val updated_by: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("tax") val tax: Tax,
    @SerializedName("categories") val categories: ArrayList<CategoriesModel>,
    @SerializedName("cart") val cart: CartInfo,
    @SerializedName("product_specifications") val product_specifications: ArrayList<ProductSpecifications>,

    @SerializedName("images") val images: ArrayList<ImageModel>

)
