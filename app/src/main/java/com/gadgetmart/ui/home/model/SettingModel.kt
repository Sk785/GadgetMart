package com.gadgetmart.ui.home.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class SettingModel(
@SerializedName("data") val data: AllData?,
@SerializedName("message") val message: String?,
@SerializedName("status") val status: Int?
)