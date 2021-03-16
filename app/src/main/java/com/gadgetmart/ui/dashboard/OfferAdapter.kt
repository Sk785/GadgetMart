package com.gadgetmart.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.Offers
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.gadgetmart.ui.search.SearchAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.banner_offer_item.view.*
import kotlinx.android.synthetic.main.subcategory_items_layout.view.*

class OfferAdapter (val context: Context?,
private var categories: ArrayList<Offers>?,
private val adapterListener: DashboardAdapterListener
) :
RecyclerView.Adapter<OfferAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferAdapter.CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.banner_offer_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (categories == null)
            0
        else
            categories.let { it!!.size }
    }

    override fun onBindViewHolder(holder: OfferAdapter.CategoryViewHolder, position: Int) {
        holder.bindView(categories!![position], adapterListener)
    }

    fun updateList(categories: ArrayList<Offers>?) {
        this.categories = categories
        notifyDataSetChanged()    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            categoryItem: Offers?,
            adapterListener: DashboardAdapterListener
        ) {
            if (categoryItem!!.image != null && categoryItem.image != "") {
//                Picasso.get()
//                    .load(categoryItem.image)
//                    .placeholder(R.drawable.ic_logo_toolbar)
//                    .into(itemView.category_image_view)
                Glide.with(itemView.context)
                    .apply { RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.default_icon)
                        .override(256, 140)
                        .fitCenter(); }
                    .load(categoryItem.image)
                    .transform(CenterCrop(), RoundedCorners(30))
                    .placeholder(R.drawable.default_icon)
                    .into(itemView.offers_image_view)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .transform(CenterCrop(), RoundedCorners(30))

                    .into(itemView.offers_image_view)
            }
            itemView.offer_txt.text=categoryItem.name

        }
    }
}