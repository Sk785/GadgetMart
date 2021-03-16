package com.gadgetmart.ui.my_address

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class AddressResult(
    @SerializedName("address") var address : AddAddressItems?,
    @SerializedName("addresses") var addresses : ArrayList<AddAddressItems>?
)