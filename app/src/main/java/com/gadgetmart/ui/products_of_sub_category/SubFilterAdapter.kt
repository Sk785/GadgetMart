package com.gadgetmart.ui.products_of_sub_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.sub_filter_item_layout.view.*

class SubFilterAdapter(val context: Context?,
                       private var productsListing : ArrayList<FilterOptionsArray>?,
                       private var dashboardAdapterListener : FilterInterface,
                       var parentPos:Int
) :
    RecyclerView.Adapter<SubFilterAdapter.CategoryViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.sub_filter_item_layout, parent, false)

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
        holder.bindView(dashboardAdapterListener, productsListing!![position], position,parentPos)
    }





    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(dashboardAdapterListener: FilterInterface, productsFilterOptions: FilterOptionsArray, position: Int,parentPos:Int) {
                itemView.sub_filter_name_text_view?.text = productsFilterOptions.name
            if(productsFilterOptions.isSelected==true){
                itemView.filter_checkbox.isChecked=true
            }else{
                itemView.filter_checkbox.isChecked=false

            }
            itemView.filter_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    dashboardAdapterListener.onClickChild(parentPos,position,true)
                }else{
                    dashboardAdapterListener.onClickChild(parentPos,position,false)


                }
            }


        }
    }

}
