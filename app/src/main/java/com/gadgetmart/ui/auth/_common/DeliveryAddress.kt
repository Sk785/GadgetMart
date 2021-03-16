package com.gadgetmart.ui.auth._common

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
 data class DeliveryAddress (

    @SerializedName("id") val id : Int,
    @SerializedName("user_id") val user_id : Int,
    @SerializedName("name") val name : String,

    @SerializedName("address_type") val address_type : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("landmark") val landmark : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("zip") val zip : String,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("country_code") val country_code : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("alt_phone") val alt_phone : String,
    @SerializedName("default") val default : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String

)