package com.gadgetmart.ui.my_address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class State(
    @SerializedName("id") var id: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("country_id") var countryId: String?,
    @SerializedName("cities") var cities: ArrayList<City>?
) : Parcelable