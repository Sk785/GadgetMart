package com.gadgetmart.ui.products_of_sub_category

import android.os.Parcelable
import com.gadgetmart.ui.category.ProductVariation
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class FiltersArray (var name: String,

                        var filter_options: ArrayList<FilterOptionsArray>,
                         var isSelected: Boolean?
)
