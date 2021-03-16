package com.gadgetmart.ui.product_details

import android.os.Parcelable
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.parceler.Parcel

@Parcelize
data class ProductSpecificationNew(
        @SerializedName("name") val name: String?,
        @SerializedName("value") val value:ArrayList<ProductSpecification>?

): Parcelable