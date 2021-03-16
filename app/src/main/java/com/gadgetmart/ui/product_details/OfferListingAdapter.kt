package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.category.OffersModel
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductSpecification
import kotlinx.android.synthetic.main.offer_item_listing.view.*
import kotlinx.android.synthetic.main.specification_sub_title.view.*

class OfferListingAdapter (val context: Context?,
                           private var productsListing : ArrayList<OffersModel>?,
                           private var dashboardAdapterListener : OfferItemClick
) :
    RecyclerView.Adapter<OfferListingAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.offer_item_listing, parent, false)

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
        holder.bindView(dashboardAdapterListener, productsListing!![position],position)
    }



    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(dashboardAdapterListener: OfferItemClick, productSpecification: OffersModel,pos:Int) {

            itemView.offername?.text = productSpecification.name


            itemView.setOnClickListener {
                dashboardAdapterListener.onOfferItemClick(pos)
            }



        }
    }

}
