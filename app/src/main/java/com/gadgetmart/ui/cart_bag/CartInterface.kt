package com.gadgetmart.ui.cart_bag

interface CartInterface {
    fun addMoreProduct(p: Int, variationId: String, price: String, count: Int)
    fun addLessProduct(p: Int, variationId: String, price: String, count: Int)
    fun clickOnItem(p: Int)
    fun clickOnView(p: Int)

}