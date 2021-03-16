package com.gadgetmart.ui.products_of_sub_category

interface FilterInterface {
    fun onClickParent(pos:Int)
    fun onClickChild(parentPos:Int, pos:Int,isSelected:Boolean)

}