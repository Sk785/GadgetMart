package com.gadgetmart.ui.order.model

import android.os.Parcelable
import com.gadgetmart.ui.cart_bag.model.Tax
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.my_address.AddAddressItems
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProducts
    (
    @SerializedName("id") val id: String?,
    @SerializedName("order_number") val order_number: String?,
    @SerializedName("order_id") val order_id: String?,
    @SerializedName("product_id") val product_id: String?,
    @SerializedName("quantity") val quantity: String?,
    @SerializedName("unit_price") val unit_price: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("tax") val tax: String?,
    @SerializedName("coupon_code") val coupon_code: String?,
    @SerializedName("coupon_discount") val coupon_discount: String?,
    @SerializedName("delivery_charges") val delivery_charges: String?,
    @SerializedName("total_amount") val total_amount: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("delivery_address") val delivery_address: AddAddressItems?,

    @SerializedName("status") val status: String?,
    @SerializedName("status_date") val status_date: String?,
    @SerializedName("status_remarks") val status_remarks: String?,
    @SerializedName("awb") val awb: String?,
    @SerializedName("shipping_agency") val shipping_agency: String?,
    @SerializedName("received_by") val received_by: String?,
    @SerializedName("tax_data") val tax_data: Tax?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("is_product_refund") val is_product_refund: Boolean,



    @SerializedName("product_data") val product_data: ProductVariation?,
    @SerializedName("order_status") val order_status: ArrayList<OrderStatus>?


): Parcelable


