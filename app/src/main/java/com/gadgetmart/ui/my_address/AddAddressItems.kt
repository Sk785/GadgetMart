package com.gadgetmart.ui.my_address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddAddressItems(
             @SerializedName("id") var id : Int?,
             @SerializedName("user_id") var user_id : Int?,
             @SerializedName("name") var name : String?,
             @SerializedName("address_type") var addressType : String?,
             @SerializedName("address1") var address1 : String?,
             @SerializedName("address2") var address2 : String?,
             @SerializedName("city") var city : String?,
             @SerializedName("state") var state : String?,
             @SerializedName("country") var country : String?,
             @SerializedName("zip") var zip : String?,
             @SerializedName("latitude") var latitude : String?,
             @SerializedName("longitude") var longitude : String?,
             @SerializedName("country_code") var country_code : String?,
             @SerializedName("phone") var phone : String?,
             @SerializedName("default") var default : String?,
             @SerializedName("created_at") var created_at : String?,
             @SerializedName("updated_at") var updated_at : String?,
             var isSelected: Boolean = false

) : Parcelable