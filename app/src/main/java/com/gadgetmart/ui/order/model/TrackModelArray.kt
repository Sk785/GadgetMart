package com.gadgetmart.ui.order.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class TrackModelArray (
    @SerializedName("id") val id: Int?,

    @SerializedName("status") val status: String?,

    @SerializedName("remarks") val remark: String?,
    @SerializedName("status_date") val status_date: String?=""


    )
