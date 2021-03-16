package com.gadgetmart.ui.dashboard

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentBatch(
    @SerializedName("id") val dealsId: Int? = 0,
    @SerializedName("product_id") val product_id: String?,
    @SerializedName("sku_id") val skuId: String?,
    @SerializedName("invoice_id") val invoiceId: String?,
    @SerializedName("batch_number") val batch_number: String?,
    @SerializedName("mfd_date") val mfd_date: String?,
    @SerializedName("expiry_date") val expiry_date: String?,
    @SerializedName("quantity") val quantity: String?,
    @SerializedName("margin_ratio") val margin_ratio: String?,
    @SerializedName("product_mrp") val product_mrp: Float?,
    @SerializedName("base_rate") var base_rate: Float?,
   var discount_rate: Float?,

    @SerializedName("balance_quantity") val balance_quantity: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("weight_unit") val weightUnit: String?,
    @SerializedName("d_length") val d_length: String?,
    @SerializedName("d_bredth") val d_breadth: String?,
    @SerializedName("d_height") val d_height: String?,
    @SerializedName("offer_type") val offer_type: String?,
    @SerializedName("offer_value") val offer_value: String?,
    @SerializedName("offer_start_date") val offer_start_date: String?,
    @SerializedName("offer_end_date") val offer_end_date: String?,
    @SerializedName("sale_order") val sale_order: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("offer_discount") val offer_discount: String?,
    @SerializedName("net_price") val net_price: String?,
    @SerializedName("save_amount") val save_amount: String?



) : Parcelable