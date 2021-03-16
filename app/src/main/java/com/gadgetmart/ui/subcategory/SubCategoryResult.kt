package com.gadgetmart.ui.subcategory

import com.gadgetmart.ui.category.CategoryItem
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class SubCategoryResult (
    @SerializedName("category") val categories: ArrayList<CategoryItem>?
)