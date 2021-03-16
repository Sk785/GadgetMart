package com.gadgetmart.data.base

import com.google.gson.annotations.SerializedName

class ApiResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T
)