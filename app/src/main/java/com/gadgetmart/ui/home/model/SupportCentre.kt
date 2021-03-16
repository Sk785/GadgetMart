package com.gadgetmart.ui.home.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class SupportCentre(
    @SerializedName("whatsapp") val whatsapp: ArrayList<String>?,
    @SerializedName("mobile") val mobile: ArrayList<String>?,
    @SerializedName("email") val email: ArrayList<String>?
)