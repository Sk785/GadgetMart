package com.gadgetmart.ui.products_of_sub_category

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult

interface ProductsOfSubContract : BaseContract{
    fun onProductsOfSubDataFound(productsOfSubResult : ProductsOfSubResult?, message: String, apiFlag : Int?)

    fun onProductAddedToWishList(wishListResult : WishListResult?, message: String)

    fun onProductRemovedFromWishList(wishListRemovedResult: WishListRemovedResult?, message: String)

    fun onProductsOfSubDataNotFound(message: String)
}