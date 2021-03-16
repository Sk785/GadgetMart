package com.gadgetmart.ui.my_address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("id") var id: String?,
    @SerializedName("sortname") var sortname: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("phonecode") var phonecode: String?,
    @SerializedName("states") var states: ArrayList<State>?
) : Parcelable