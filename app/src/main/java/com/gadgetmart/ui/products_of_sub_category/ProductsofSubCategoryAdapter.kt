package com.gadgetmart.ui.products_of_sub_category

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.ProductVariation
import kotlinx.android.synthetic.main.products_of_subcategory_items_layout.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductsofSubCategoryAdapter(
    val context: Context?,
    private var productsListing: ArrayList<ProductVariation>?,
    private var dashboardAdapterListener: ProductsAdapterListener
) :
    RecyclerView.Adapter<ProductsofSubCategoryAdapter.CategoryViewHolder>() {

    private var productID: Int? = null
    private var wishListID: Int? = null
    private var wishListData: ProductDataWishlist? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.products_of_subcategory_items_layout, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (productsListing == null) {
            0
        } else {
            productsListing.let { it!!.size }
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(dashboardAdapterListener, productsListing!![position], position)
    }

    fun updateList(productsListing: ArrayList<ProductVariation>?) {
        this.productsListing = productsListing
        notifyDataSetChanged()
    }

    fun updateWishList(productID: Int?, wishListID: Int?, wishListData: ProductDataWishlist) {
        this.productID = productID
        this.wishListID = wishListID
        this.wishListData = wishListData
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var variation: ProductVariation? = null

        fun bindView(
            dashboardAdapterListener: ProductsAdapterListener,
            productsDataItems: ProductVariation,
            position: Int
        ) {
            variation = productsDataItems
            if (productsDataItems?.productImages != null) {
                if (productsDataItems?.productImages!![0].name != null || productsDataItems?.productImages!![0].name != "") {
                    Glide.with(itemView.context)
                        .apply {
                            RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_icon)
                                .override(256, 140)
                                .fitCenter();
                        }
                        .load(productsDataItems?.productImages!![0].name)
                        .placeholder(R.drawable.default_icon)
                        .into(itemView.products_of_subcategory_image_view)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(itemView.products_of_subcategory_image_view)
                }
            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if (productsDataItems.currentBatch != null)
                if (productsDataItems.offer_available!!) {
                    itemView.products_of_subcategory_discount.visibility = View.VISIBLE

                    itemView.products_of_subcategory_discount.paintFlags =
                        itemView.products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = itemView.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(productsDataItems?.currentBatch?.product_mrp))
                    itemView.products_of_subcategory_discount.text = str
                    itemView.products_of_subcategory_price.text =
                        "\u20B9" + df.format(productsDataItems?.offer_discount_price)
                } else {


                    if (productsDataItems?.currentBatch?.product_mrp!! == productsDataItems?.currentBatch?.base_rate!!) {
                        itemView.products_of_subcategory_price.text =
                            "\u20B9" + df.format(productsDataItems?.currentBatch?.product_mrp)

                        itemView.products_of_subcategory_discount.visibility = View.INVISIBLE
                    } else {
                        itemView.products_of_subcategory_discount.visibility = View.VISIBLE

                        itemView.products_of_subcategory_discount.paintFlags =
                            itemView.products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(productsDataItems?.currentBatch?.product_mrp))
                        itemView.products_of_subcategory_discount.text = str
                        itemView.products_of_subcategory_price.text =
                            "\u20B9" + df.format(productsDataItems?.currentBatch?.base_rate)
                    }
                }
            itemView.products_of_subcategory_tax?.visibility = View.GONE
//            if (productsDataItems.taxable.equals("yes", ignoreCase = true)) {
//                val taxDetails: ProductItemsTax = productsDataItems.tax!!
//                itemView.products_of_subcategory_tax?.visibility = View.VISIBLE
//                if (taxDetails.rate.toString().isNotEmpty()) {
//                    itemView.products_of_subcategory_tax.text =
//                        taxDetails.name + " " + taxDetails.rate + "%"
//                }
//            } else {
//                itemView.products_of_subcategory_tax?.visibility = View.GONE
//            }

            itemView.products_of_subcategory_text_name.text = productsDataItems.product_title
            val favDrawable: Drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.fav_enable)!!
            val removeFavDrawable: Drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.fav_disable)!!
            if (productsDataItems.offer_available!!) {
                itemView.discount_txt.visibility = View.VISIBLE
            } else {
                itemView.discount_txt.visibility = View.GONE

            }

            Log.e("WishList ::: ", "" + variation?.wishList)
            if (productsDataItems?.wishList != null) {
                productsDataItems.isAddedToWishList = true
                itemView.products_of_subcategory_fav_image_view.setImageDrawable(favDrawable)
            } else {
                productsDataItems.isAddedToWishList = false
                itemView.products_of_subcategory_fav_image_view.setImageDrawable(removeFavDrawable)
            }
            itemView.setOnClickListener {
                dashboardAdapterListener.onAdapterItemTapped(
                    "" + productsDataItems.variationId,
                    "",
                    position, productsDataItems.variationId
                )
            }

            itemView.products_of_subcategory_fav_image_view?.setOnClickListener {
                val wishlist = productsDataItems?.wishList
                Log.e("WishList :: ", "OnClick")
                if (wishlist != null) {
                    Log.e("WishList :: ", "not null")
                    productsDataItems.isAddedToWishList = false
                    itemView.products_of_subcategory_fav_image_view.setImageDrawable(
                        removeFavDrawable
                    )
                    dashboardAdapterListener.onAdapterItemTapped(
                        "" + wishlist.wishList_id,
                        "removedFromWishList",
                        position, wishlist.variation_id
                    )
                } else {
                    Log.e("WishList :: ", "null")
                    productsDataItems.isAddedToWishList = true
                    itemView.products_of_subcategory_fav_image_view.setImageDrawable(favDrawable)
                    dashboardAdapterListener.onAdapterItemTapped(
                        "" + productsDataItems.variationId,
                        "addedToWishList",
                        position, productsDataItems.variationId
                    )
                }
            }

            if (productsDataItems.cart != null) {
                itemView.products_of_subcategory_addto_cart.setText("Go to bag ")

            } else {
                itemView.products_of_subcategory_addto_cart.setText("Add to bag")

            }
            itemView.products_of_subcategory_addto_cart.setOnClickListener {
                dashboardAdapterListener.onAddToBag(
                    position,
                    productsDataItems.variationId,
                    itemView.products_of_subcategory_addto_cart.text.toString(),productsDataItems.product_title!!
                )
                itemView.products_of_subcategory_addto_cart.setText("Go to bag ")

            }

            if (variation?.rating != null) {
                var avg = variation?.rating

                itemView.products_of_subcategory_rating_bar.rating = avg!!.toFloat()


            }


            Log.e("AddedToWishList :::: ", " " + productsDataItems.isAddedToWishList)
        }
    }
}