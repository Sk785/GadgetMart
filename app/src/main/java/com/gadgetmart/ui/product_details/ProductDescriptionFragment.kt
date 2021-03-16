package com.gadgetmart.ui.product_details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.databinding.FragmentProductDescriptionBinding
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import org.parceler.Parcels

class ProductDescriptionFragment : BaseFragment<FragmentProductDescriptionBinding>() , DashboardAdapterListener{

    private var type: Int ? = null
    private var productsDataItems : ProductVariation ?= null
    private var variation: ProductVariation? = null
    private var ARG_TYPE_1 = "typeoffragment"
    private var ARG_TYPE_2 = "productDetail"
    private var productSpecsAdapter : ProductSpecsAdapter ?= null
    private var productSpecifications : ArrayList<ProductSpecificationNew> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        type = bundle?.getInt(ARG_TYPE_1)
        productsDataItems = bundle?.getParcelable(ARG_TYPE_2)
        productSpecifications = bundle?.getParcelableArrayList("specifications")
       /*
        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }*/
    }

    override fun openInternetDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun newInstance(key:String, type:Int){
            var bundle = Bundle()

        }
        /*
        fun newInstance(key : String, type: Int) =
            ProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, type)
                }
            }*/
    }

    override fun getContentView(): Int {
        return R.layout.fragment_product_description
    }

    override fun initView(binding: FragmentProductDescriptionBinding) {
        Log.e("Type "," " + type)
        initRecyclerView(binding)
        if (type == 0){
            binding.frame1.visibility = View.VISIBLE
            binding.frame2.visibility = View.GONE
//            if (productsDataItems?.description!=null){
//                binding.productDescriptionText.text = productsDataItems?.description
//            }else{
                binding.productDescriptionText.text = productsDataItems?.short_description
            //}

        }else if (type == 1){
            binding.frame1.visibility = View.GONE
            binding.frame2.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView(binding: FragmentProductDescriptionBinding){
        val subCategoriesLayoutManager = LinearLayoutManager(activity)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.productSpecsRecyclerview.layoutManager = subCategoriesLayoutManager

        productSpecsAdapter = ProductSpecsAdapter(activity!!, productSpecifications,this)
        binding.productSpecsRecyclerview.adapter = productSpecsAdapter
    }

    override fun initListeners(binding: FragmentProductDescriptionBinding) {

    }

    override fun initPresenters() {

    }

    override fun onAdapterItemTapped(adapterItem: String?, categoryName: String?,variationId:String?) {

    }

    override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
    }
}
