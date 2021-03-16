package com.gadgetmart.ui.products_of_sub_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.product_filter_item_layout.view.*

class FilterAdapter (val context: Context?,
                     private var productsListing : ArrayList<FiltersArray>?,
                     private var dashboardAdapterListener : FilterInterface) :
    RecyclerView.Adapter<FilterAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.product_filter_item_layout, parent, false)

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
        holder.bindView(dashboardAdapterListener, productsListing!![position], position)
    }



    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(dashboardAdapterListener: FilterInterface, productsFilterOptions: FiltersArray, position: Int) {
            itemView.filter_name_text_view?.text = productsFilterOptions.name
            itemView.setOnClickListener {

                dashboardAdapterListener.onClickParent(position)
            }
        }
    }

}
