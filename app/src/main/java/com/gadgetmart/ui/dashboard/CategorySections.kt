package com.gadgetmart.ui.dashboard

import com.gadgetmart.ui.category.PopularGadgetOfferItem
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CategorySections(
    @SerializedName("popular") val popularGadgets: ArrayList<PopularGadgetOfferItem>?,
    @SerializedName("deals") val deals: ArrayList<CategoryDeals>?,
    @SerializedName("offers") val offers: ArrayList<PopularGadgetOfferItem>?
)
