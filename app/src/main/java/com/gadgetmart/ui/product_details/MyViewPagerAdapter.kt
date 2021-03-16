package com.gadgetmart.ui.product_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import org.parceler.Parcels

class MyViewPagerAdapter(fm : FragmentManager,var productsDataItems: ProductVariation?,var specifications:ArrayList<ProductSpecificationNew>) : FragmentPagerAdapter(fm) {
    private var TYPEDISCRIPTION = 0
    private var TYPE_SPECIFICATION = 1

    override fun getItem(position: Int): Fragment {
            var fragment : Fragment ?= null
        if (position == 0){
            fragment = ProductDescriptionFragment()
            val bundle = Bundle()
            bundle.putInt("typeoffragment", TYPEDISCRIPTION)
            bundle.putParcelable("productDetail", productsDataItems)
            bundle.putParcelableArrayList("specifications",specifications)


            fragment.arguments = bundle
        }else if (position == 1){
            fragment = ProductDescriptionFragment()
            val bundle = Bundle()
            bundle.putInt("typeoffragment", TYPE_SPECIFICATION)
            bundle.putParcelable("productDetail", productsDataItems)
            bundle.putParcelableArrayList("specifications",specifications)

            fragment.arguments = bundle
        }

        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title : String ?= null
        if (position == 0){
            title = "Description"
        }else if(position == 1){
            title = "Product Details"
        }
        return title
    }

}