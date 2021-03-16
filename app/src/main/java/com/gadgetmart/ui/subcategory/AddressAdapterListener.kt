package com.gadgetmart.ui.subcategory

interface AddressAdapterListener {
    fun onAdapterItemTapped(adapterItem: Int?, categoryName : String?, itemPosition : Int? )
    fun onAdapterItemClick(id : Int,name:String,pincode:String)
    fun onSetAsDefaultMenuItemSelected(addressId: String?)
}