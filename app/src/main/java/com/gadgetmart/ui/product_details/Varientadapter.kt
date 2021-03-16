package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.products_of_sub_category.ProductsAdapterListener
import com.gadgetmart.ui.products_of_sub_category.VariationAdapter
import kotlinx.android.synthetic.main.variation_item.view.*
import kotlinx.android.synthetic.main.varient_item.view.*

class Varientadapter(
    val context: Context?,
    private var variations: ArrayList<SelectedVarient>?,
    private var dashboardAdapterListener: VarientItemClickInterface
) :
    RecyclerView.Adapter<Varientadapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.varient_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variations?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(dashboardAdapterListener, variations!![position], variations!!.size)
    }

    fun updateList(variations: ArrayList<SelectedVarient>?) {
        this.variations = variations
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            dashboardAdapterListener: VarientItemClickInterface,
            variation: SelectedVarient,
            size: Int
        ) {
itemView.variation_name.setText(variation.variation_name+":")

            itemView.variation_value.setText(variation.variation_value)
            itemView.variation_size.setText("Show All")


            itemView.setOnClickListener {

                dashboardAdapterListener.onAdapterItemTapped(

                )
            }
        }
    }

}
