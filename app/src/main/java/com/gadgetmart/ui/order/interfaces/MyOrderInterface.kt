package com.gadgetmart.ui.order.interfaces

import java.text.FieldPosition

interface MyOrderInterface {
    fun parentClick(pod:Int)
    fun childClick(parentPos:Int,childPos:Int)
    fun onItemClicked(position: Int)

    fun openReviewScreen(parentPos:Int,childPos:Int)
    fun openCancelScreen(parentPos:Int,childPos:Int,type:Int)
}