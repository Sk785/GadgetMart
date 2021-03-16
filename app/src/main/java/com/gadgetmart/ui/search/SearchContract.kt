package com.gadgetmart.ui.search

import com.gadgetmart.base.BaseContract

interface SearchContract : BaseContract {

    fun onAddedToSearchList(message: SearchProduct)

    fun onNotAddedToSearchList(message: String)
}