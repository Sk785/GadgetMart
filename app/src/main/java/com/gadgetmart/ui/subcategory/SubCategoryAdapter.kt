package com.gadgetmart.ui.subcategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.SubCategoryItem
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import kotlinx.android.synthetic.main.subcategory_items_layout.view.*

class SubCategoryAdapter (
    val context: Context?,
    private var categories: ArrayList<SubCategoryItem>?,
    private val adapterListener: DashboardAdapterListener
) :
    RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.subcategory_items_layout, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (categories == null)
            0
        else
            categories.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(categories!![position], adapterListener)
    }

    fun updateList(categories: ArrayList<SubCategoryItem>?) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            categoryItem: SubCategoryItem?,
            adapterListener: DashboardAdapterListener
        ) {

            if (categoryItem!!.itemImage != null && categoryItem.itemImage != "") {
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
                    .load(categoryItem.itemImage)
                    .placeholder(R.drawable.default_icon)
                    .into(itemView.ctgry_logo_image_view)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .into(itemView.ctgry_logo_image_view)
            }
            itemView.ctgry_text_name.text = categoryItem.itemName
            var products : String = ""
            if (categoryItem.products_count == 1){
                products = "product"
            }else{
                products = "products"
            }
            itemView.ctgry_text_products.text = categoryItem.sku_count.toString() + " " + products

            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(categoryItem.itemId, categoryItem.itemName,"")
            }
        }
    }
}