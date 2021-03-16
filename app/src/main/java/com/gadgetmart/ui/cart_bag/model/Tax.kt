package com.gadgetmart.ui.cart_bag.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tax(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("taxId") val status: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("rate") val rate: Int?,
    @SerializedName("enabled") val enabled: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?
) : Parcelable