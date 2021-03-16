package com.gadgetmart.ui.subcategory

import com.gadgetmart.base.BaseContract

interface SubCategoryContract : BaseContract {
    fun onSubCategoryDataFound(subCategoryResult : SubCategoryResult?, message: String)

    fun onSubCategoryDataNotFound(message: String)
}