package com.gadgetmart.ui.order.model

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel


@Parcel
data class TrackListModel (


    @SerializedName("orderStatus") val orderStatus: ArrayList<TrackModelArray>?
)
