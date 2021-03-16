package com.gadgetmart.ui.wishlist

import com.gadgetmart.ui.products_of_sub_category.ProductDataWishlist
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class WishListRemovedResult (
    @SerializedName("my_wish_list") val my_wish_list : ArrayList<ProductDataWishlist>
)