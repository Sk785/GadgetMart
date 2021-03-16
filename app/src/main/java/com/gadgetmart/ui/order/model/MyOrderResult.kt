package com.gadgetmart.ui.order.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class MyOrderResult (
    @SerializedName("data") val data: DataModel?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Int?)