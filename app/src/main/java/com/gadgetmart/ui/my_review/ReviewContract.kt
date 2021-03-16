package com.gadgetmart.ui.my_review

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.search.SearchProduct


interface ReviewContract : BaseContract {

    fun onReviewFound(message: ReviewData)

    fun onReviewNotFound(message: String)
}