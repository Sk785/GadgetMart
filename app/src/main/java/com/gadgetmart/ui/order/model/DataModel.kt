package com.gadgetmart.ui.order.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class DataModel (
    @SerializedName("current_page") val current_page: String?,
    @SerializedName("first_page_url") val first_page_url: String?,
    @SerializedName("from") val from: String?,
    @SerializedName("last_page") val last_page: String?,
    @SerializedName("last_page_url") val last_page_url: String?,
    @SerializedName("next_page_url") val next_page_url: String?,
    @SerializedName("path") val path: String?,
    @SerializedName("per_page") val per_page: String?,
    @SerializedName("prev_page_url") val prev_page_url: String?,
    @SerializedName("to") val to: String?,
    @SerializedName("total") val total: String?,
    @SerializedName("data") val data: ArrayList<DataModelArray>?
)
