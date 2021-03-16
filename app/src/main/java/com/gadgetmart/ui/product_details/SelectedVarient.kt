package com.gadgetmart.ui.product_details

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedVarient (

    @SerializedName("variation_value") var variation_value: String? = "",
    @SerializedName("variation_name") var variation_name: String?
) : Parcelable