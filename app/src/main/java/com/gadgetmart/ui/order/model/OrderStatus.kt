package com.gadgetmart.ui.order.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderStatus (
    @SerializedName("id") val id: String?

): Parcelable

