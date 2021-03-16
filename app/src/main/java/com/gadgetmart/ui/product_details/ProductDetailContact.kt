package com.gadgetmart.ui.product_details

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult

interface ProductDetailContact : BaseContract {
    fun onProductDetailDataFound(productDetailResult: ProductDetailResult?, message: String)

    fun onProductDetailDataNotFound(message: String)

    fun onProductAddedToWishList(wishListResult : WishListResult?, message: String)

    fun onProductRemovedFromWishList(wishListRemovedResult: WishListRemovedResult?, message: String)


    fun productAddToRecent( message: String)

    fun onProductRecentNotAdded(message: String)
}