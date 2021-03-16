package com.gadgetmart.ui.cart_bag.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentBatch(
    @SerializedName("balance_quantity")val balance_quantity: Int?,
    @SerializedName("base_rate")val base_rate: Float?,
    @SerializedName("batch_number")val batch_number: String?,
    @SerializedName("created_at")val created_at: String?,
    @SerializedName("enabled")val enabled: String?,
    @SerializedName("expiry_date")val expiry_date: String?,
    @SerializedName("id")val id: Int?,
    @SerializedName("margin_ratio")val margin_ratio: Double?,
    @SerializedName("mfd_date")val mfd_date: String?,
    @SerializedName("net_price")val net_price: Float?,
    @SerializedName("offer_discount")val offer_discount: Float?,
    @SerializedName("offer_end_date")val offer_end_date: String?,
    @SerializedName("offer_start_date")val offer_start_date: String?,
    @SerializedName("offer_type")val offer_type: String?,
    @SerializedName("offer_value")val offer_value: String?,
    @SerializedName("product_id")val product_id: Int?,
    @SerializedName("product_mrp")val product_mrp: Int?,
    @SerializedName("quantity")val quantity: Int?,
    @SerializedName("sale_order")val sale_order: Int?,
    @SerializedName("updated_at")val updated_at: String?
) : Parcelable