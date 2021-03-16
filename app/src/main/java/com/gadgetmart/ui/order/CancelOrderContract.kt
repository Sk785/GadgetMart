package com.gadgetmart.ui.order

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.my_review.ReviewData
import java.text.FieldPosition

interface CancelOrderContract : BaseContract{

    fun onCancelOrderSuccess(message: String)

    fun onCancelOrderFailure(message: String)

    fun noNetworkFound(message: String)

}