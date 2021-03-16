package com.gadgetmart.ui.wishlist

interface WishListAdapterListener {
    fun onAdapterItemTapped(adapterItem: Int?, categoryName : String?, itemPosition : Int? )
    fun onAddToBag(adapterItem: Int?, product_id : String?,title:String)

}