package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductItemsTax (
    @SerializedName("id") val id : Int? = 0,
    @SerializedName("name") val name : String?,
    @SerializedName("taxId") val taxId : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("rate") val rate : Int? = 0,
    @SerializedName("enabled") val enabled : String?,
    @SerializedName("created_at") val created_at : String?,
    @SerializedName("updated_at") val updated_at : String?
    ) : Parcelable

