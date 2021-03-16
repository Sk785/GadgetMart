package com.gadgetmart.ui.search

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.category.SubCategoryItem
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.ui.subcategory.SubCategoryAdapter
import com.gadgetmart.util.PreferenceManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.products_of_subcategory_items_layout.view.*
import kotlinx.android.synthetic.main.subcategory_items_layout.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class SearchAdapter (
    val context: Context?,
    private var categories: ArrayList<ProductVariation>?,
    private val adapterListener: DashboardAdapterListener
) :
    RecyclerView.Adapter<SearchAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.products_of_subcategory_items_layout, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (categories == null)
            0
        else
            categories.let { it!!.size }
    }

    override fun onBindViewHolder(holder: SearchAdapter.CategoryViewHolder, position: Int) {
        holder.bindView(categories!![position], adapterListener)
    }

    fun updateList(categories: ArrayList<ProductVariation>?) {
        this.categories = categories
notifyDataSetChanged()    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            categoryItem: ProductVariation?,
            adapterListener: DashboardAdapterListener
        ) {
            val json=Gson()
            Log.e("categories size",json.toJson(categoryItem))

            if (categoryItem!!.productImages!![0].name != null &&categoryItem!!.productImages!![0].name!= "") {
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
                    .load(categoryItem!!.productImages!![0].name)
                    .placeholder(R.drawable.default_icon)
                    .into(itemView.products_of_subcategory_image_view)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .into(itemView.products_of_subcategory_image_view)
            }
//            itemView.ctgry_text_name.text = categoryItem.itemName
//            var products : String = ""
//            if (categoryItem.products_count == 1){
//                products = "product"
//            }else{
//                products = "products"
//            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            itemView.products_of_subcategory_addto_cart.visibility=View.INVISIBLE
            itemView.products_of_subcategory_fav_image_view.visibility=View.INVISIBLE

            if (categoryItem.currentBatch != null) {
                if (categoryItem.offer_available!!) {
                    itemView.products_of_subcategory_discount.visibility = View.VISIBLE

                    itemView.products_of_subcategory_discount.paintFlags =
                        itemView.products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = itemView.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(categoryItem?.currentBatch?.product_mrp))
                    itemView.products_of_subcategory_discount.text = str
                    itemView.products_of_subcategory_price.text =
                        "\u20B9" + df.format(categoryItem?.offer_discount_price)
                } else {
                    if (categoryItem?.currentBatch?.product_mrp!! == categoryItem?.currentBatch?.base_rate!!) {
                        itemView.products_of_subcategory_price.text =
                            "\u20B9" + df.format(categoryItem?.currentBatch?.product_mrp)

                        itemView.products_of_subcategory_discount.visibility = View.INVISIBLE
                    } else {
                        itemView.products_of_subcategory_discount.visibility = View.VISIBLE

                        itemView.products_of_subcategory_discount.paintFlags =
                            itemView.products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(categoryItem?.currentBatch?.product_mrp))
                        itemView.products_of_subcategory_discount.text = str
                        itemView.products_of_subcategory_price.text =
                            "\u20B9" + df.format(categoryItem?.currentBatch?.base_rate)
                    }
                }
            }

            if(categoryItem?.rating!=null){
                var avg=categoryItem?.rating

                itemView.products_of_subcategory_rating_bar.rating=avg!!.toFloat()


            }
            //itemView.ctgry_text_products.text ="₹" +df.format(categoryItem.currentBatch!!.base_rate)
            itemView.products_of_subcategory_text_name.text=categoryItem.product_title
            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(categoryItem.variationId.toString(), categoryItem.product_title,categoryItem.variationId)
            }
        }
    }

}