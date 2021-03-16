package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import kotlinx.android.synthetic.main.specification_sub_title.view.*

class ProductSpecsAdapter (val context: Context?,
                           private var productsListing : ArrayList<ProductSpecificationNew>?,
                           private var dashboardAdapterListener : DashboardAdapterListener
) :
    RecyclerView.Adapter<ProductSpecsAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.specification_sub_title, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (productsListing == null){
            0
        }else {
            productsListing.let { it!!.size }
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(dashboardAdapterListener, productsListing!![position])
    }

    fun updateList(productsListing: ArrayList<ProductSpecificationNew>?) {
        this.productsListing = productsListing
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(dashboardAdapterListener: DashboardAdapterListener, productSpecification: ProductSpecificationNew) {

            itemView.product_specs_title_name?.text = productSpecification.name

            val subCategoriesLayoutManager = LinearLayoutManager(itemView.context)

            subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

            itemView.product_specs_sub_recycler_view.layoutManager = subCategoriesLayoutManager

            var productSubSpecsListing = productSpecification.value
            val productSpecsSubAdapter = ProductSpecsSubAdapter(itemView.context, productSubSpecsListing)
            itemView.product_specs_sub_recycler_view.adapter = productSpecsSubAdapter
            itemView.product_specs_sub_recycler_view.isNestedScrollingEnabled=false

        }
    }

}
