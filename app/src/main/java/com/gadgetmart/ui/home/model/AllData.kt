package com.gadgetmart.ui.home.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class AllData (
    @SerializedName("help") val help: String?,
    @SerializedName("terms_conditions") val terms_conditions: String?,
    @SerializedName("privacy_policy") val privacy_policy: String?,
    @SerializedName("support_centre") val support_centre: SupportCentre?
)