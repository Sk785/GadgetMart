package com.gadgetmart.ui.order.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class ImageModel (
    @SerializedName("id") val id: String

)