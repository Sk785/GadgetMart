package com.gadgetmart.ui.category

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class PopularGadgetOfferItem(
    @SerializedName("id") val productId: String?,
    @SerializedName("merchant_id") val merchantId: String?,
    @SerializedName("product_code") val productCode: String?,
    @SerializedName("name") val productName: String?,
    @SerializedName("short_description") val shortDescription: String?,
    @SerializedName("description") val productDescription: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("manage_stock") val manageStock: String?,
    @SerializedName("stock_quantity") val stockQuantity: Int?,
    @SerializedName("min_stock_quantity") val minStockQuantity: String?,
    @SerializedName("weight") val productWeight: Float?,
    @SerializedName("related_files") val relatedFiles: String?,
    @SerializedName("regular_price") val regularPrice: Float?,
    @SerializedName("sale_price") val salePrice: Float?,
    @SerializedName("sale_start") val saleStart: String?,
    @SerializedName("sale_end") val saleEnd: String?,
    @SerializedName("taxable") val taxable: String?,
    @SerializedName("tax_id") val taxId: String?,
    @SerializedName("image") val productImage: String?,
    @SerializedName("related_products") val relatedProducts: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("sort_order") val sortOrder: String?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("popular") val popular: String?,
    @SerializedName("categories") val subCategoryItem: ArrayList<SubCategoryItem>?,
    @SerializedName("images") val productImages: List<PopularGadgetImage>?,
    @SerializedName("variations") val variations: ArrayList<ProductVariation>


)