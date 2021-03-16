package com.gadgetmart.ui.products_of_sub_category

interface ProductsAdapterListener {
    fun onAdapterItemTapped(adapterItem: String?, categoryName : String? , itemPosition : Int?,variationId:String?)
    fun onWish(adapterItem: Int?, categoryName : String?)
    fun onAddToBag(adapterItem: Int?, product_id : String?,text:String,title:String)


}