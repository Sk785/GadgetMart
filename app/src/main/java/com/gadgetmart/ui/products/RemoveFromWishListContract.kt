package com.gadgetmart.ui.products

import com.gadgetmart.base.BaseContract

interface RemoveFromWishListContract : BaseContract {

    fun onRemovedFromWishList(message: String)

    fun onNotRemovedFromWishList(message: String)
}