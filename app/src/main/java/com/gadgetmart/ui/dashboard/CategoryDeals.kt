package com.gadgetmart.ui.dashboard

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CategoryDeals(
    @SerializedName("id") val dealsId : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("sub_title") val sub_title : String?,
    @SerializedName("image") val dealsImage : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("start_date") val start_date : String?,
    @SerializedName("end_date") val end_date : String?,
    @SerializedName("banner_type") val banner_type : Int?


)
