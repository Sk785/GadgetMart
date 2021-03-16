package com.gadgetmart.ui.products

import com.google.gson.annotations.SerializedName

open class Pagination(
    @SerializedName("current_page") val current_page: Int? = 0,
    @SerializedName("first_page_url") val first_page_url: String? = null,
    @SerializedName("from") val from: String? = null,
    @SerializedName("last_page") val last_page: Int? = 0,
    @SerializedName("next_page_url") val next_page_url: String? = null,
    @SerializedName("path") val path: String? = null,
    @SerializedName("per_page") val per_page: Int? = 0,
    @SerializedName("prev_page_url") val prev_page_url: String? = null,
    @SerializedName("to") val to: String? = null,
    @SerializedName("total") val total: Int? = 0
)