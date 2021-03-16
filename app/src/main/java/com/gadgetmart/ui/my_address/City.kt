package com.gadgetmart.ui.my_address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    @SerializedName("id") var id: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("state_id") var stateId: String?
) : Parcelable