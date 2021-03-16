package com.gadgetmart.ui.order

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.order.model.DataModel

interface MyOrderContract : BaseContract {
    fun onMyOrderDataFound(orderData : DataModel?, message: String, apiFlag : Int?)


    fun onMyOrderDataNotFound(message: String)
}