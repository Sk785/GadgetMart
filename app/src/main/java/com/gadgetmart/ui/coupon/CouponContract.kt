package com.gadgetmart.ui.coupon

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult


interface CouponContract : BaseContract {

    fun onReviewFound(authResult : AuthResult?, message: String)

    fun onReviewNotFound(message: String)
}