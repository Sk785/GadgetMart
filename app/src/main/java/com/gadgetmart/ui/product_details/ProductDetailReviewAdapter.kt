package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.category.ProductReviewModel
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import kotlinx.android.synthetic.main.product_detail_review_item.view.*
import kotlinx.android.synthetic.main.specification_sub_title.view.*

class ProductDetailReviewAdapter (val context: Context?,
                                  private var productsListing : ArrayList<ProductReviewModel>?

) :
        RecyclerView.Adapter<ProductDetailReviewAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
                LayoutInflater.from(context)
                        .inflate(R.layout.product_detail_review_item, parent, false)

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

    fun updateList(productsListing: ArrayList<ProductReviewModel>?) {
        this.productsListing = productsListing
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(productSpecification: ProductReviewModel) {

            itemView.rating?.text = productSpecification.rating.toString()

          itemView.rating_name.text=productSpecification.title.toString()
            itemView.rating_desc.text=productSpecification.description.toString()

        }
    }

}
