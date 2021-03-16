package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import kotlinx.android.synthetic.main.specification_sub_details.view.*

class ProductSpecsSubAdapter(val context: Context?,
                             private var productsListing : ArrayList<ProductSpecification>?
) :
    RecyclerView.Adapter<ProductSpecsSubAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.specification_sub_details, parent, false)

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
        holder.bindView(productsListing!![position])
    }

    fun updateList(productsListing: ArrayList<ProductSpecification>?) {
        this.productsListing = productsListing
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(productSubSpecification: ProductSpecification) {
                itemView.product_specs_item_label.text = productSubSpecification.name
                itemView.product_specs_item_name.text = productSubSpecification.value
        }
    }
}
