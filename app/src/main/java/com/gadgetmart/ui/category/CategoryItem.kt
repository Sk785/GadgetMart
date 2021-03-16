package com.gadgetmart.ui.category

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class CategoryItem(
    @SerializedName("id") val categoryId : String?,
    @SerializedName("parent_id") val parentId : String?,
    @SerializedName("name") val categoryName : String?,
    @SerializedName("image") val categoryImage : String?,



    @SerializedName("sub_categories") val subCategories: ArrayList<SubCategoryItem>?
)