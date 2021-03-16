package com.gadgetmart.data.base

import com.google.gson.annotations.SerializedName

class ApiListResponse<T>(
    @SerializedName("status") val status: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ArrayList<T>?
)