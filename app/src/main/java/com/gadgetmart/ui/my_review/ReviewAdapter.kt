package com.gadgetmart.ui.my_review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import kotlinx.android.synthetic.main.review_list_item.view.*
import kotlinx.android.synthetic.main.subcategory_items_layout.view.*

class ReviewAdapter (
    val context: Context?,
    private val adapterListener: ReviewInterface,
var reviewList:ArrayList<ReviewList>
) :
    RecyclerView.Adapter<ReviewAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.review_list_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView( position, adapterListener,reviewList[position])
    }



    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            pos:Int,
            adapterListener: ReviewInterface,
            reviewList:ReviewList
        ) {

            if (reviewList.variation.productImages!![0].name != null && reviewList.variation.productImages!![0].name != "") {
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
                    .load(reviewList.variation.productImages!![0].name)
                    .placeholder(R.drawable.default_icon)
                    .into(itemView.productsImage)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .into(itemView.productsImage)
            }
            itemView.reviewTitile.text=reviewList.variation.product_title
            if(reviewList.description==""||reviewList.description.equals("null")){
                itemView.product_desc.visibility=View.GONE
            }else{
                itemView.product_desc.visibility=View.VISIBLE

                itemView.product_desc.text=reviewList.description

            }
            itemView.productsMessage.text=reviewList.title
            itemView.review_rating_bar.rating=reviewList.rating.toFloat()


            itemView.editLayout.setOnClickListener {
                adapterListener.onEditClick(pos);
            }
            itemView.deleteLayout.setOnClickListener {
                adapterListener.onDeleteClick(pos);
            }

       }



    }
}