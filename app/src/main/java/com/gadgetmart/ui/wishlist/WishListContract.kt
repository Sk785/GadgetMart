package com.gadgetmart.ui.wishlist

import com.gadgetmart.base.BaseContract

interface WishListContract : BaseContract{
    fun onWishListDataFound(wishListRemovedResult : WishListRemovedResult?, message: String, apiFlag : Int, status : Int?)

    fun onWisListDataNotFound(message: String)
}