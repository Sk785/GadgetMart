package com.gadgetmart.ui.product_details

import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class ProductDetailResult(
        @SerializedName("product") val product: ProductsDataItems?,
        @SerializedName("default_variant") val default_variant: ProductVariation?,
        @SerializedName("selected_variant") val selected_variant:ArrayList<SelectedVarient>?,
        @SerializedName("variants") val variants:ArrayList<VarientModel>?,
        @SerializedName("product_specifications") val product_specifications:ArrayList<ProductSpecificationNew>?,


        @SerializedName("cart_count") val cartCount: String?
)