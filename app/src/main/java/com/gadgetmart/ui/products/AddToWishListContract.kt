package com.gadgetmart.ui.products

import com.gadgetmart.base.BaseContract

interface AddToWishListContract : BaseContract {

    fun onAddedToWishList(message: String)

    fun onNotAddedToWishList(message: String)
}