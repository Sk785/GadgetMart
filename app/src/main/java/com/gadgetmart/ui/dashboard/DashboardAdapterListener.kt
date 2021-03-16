package com.gadgetmart.ui.dashboard

interface DashboardAdapterListener {

    fun onAdapterItemTapped(adapterItem: String?, categoryName : String?,variationId:String?)
    fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName : String?)

}