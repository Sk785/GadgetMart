package com.gadgetmart.ui.products

import com.gadgetmart.base.BaseContract

interface ProductsContract : BaseContract {

    fun onProductsDataFound(sectionProduct: SectionProductData?, message: String)
    fun onPopularProductsDataFound(sectionProduct: PopularSectionProductData?, message: String)

    fun onProductsDataNotFound(message: String)
}