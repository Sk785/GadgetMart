package com.gadgetmart.ui.products

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
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import kotlinx.android.synthetic.main.product_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductsAdapter(
    val context: Context?,
    private var gadgets: ArrayList<ProductVariation>?,
    private val adapterListener: DashboardAdapterListener
) :
    RecyclerView.Adapter<ProductsAdapter.OffersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.product_list_item, parent, false)

        return OffersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (gadgets == null || gadgets?.size == 0) 0
        else gadgets!!.size
    }

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        holder.bindView(gadgets!![position], adapterListener)
    }

    fun updateList(gadgets: ArrayList<ProductVariation>?) {
        this.gadgets = gadgets
        notifyDataSetChanged()
    }

    class OffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            gadget: ProductVariation?,
            adapterListener: DashboardAdapterListener
        ) {
            if (!gadget?.productImages.isNullOrEmpty()) {
                if (gadget!!.productImages?.get(0)?.name != null && gadget.productImages?.get(0)?.name != "") {
                    Glide.with(itemView.context)
                        .apply {
                            RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_icon)
                                .override(256, 140)
                                .fitCenter();
                        }
                        .load(gadget.productImages?.get(0)?.name)
                        .placeholder(R.drawable.default_icon)
                        .into(itemView.product_image_view)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(itemView.product_image_view)
                }
            }
            /*if (gadget.salePrice == null || gadget.salePrice.toString().isEmpty()) {
                val str = itemView.context.resources.getText(R.string.us_dollar).toString().plus(gadget.regularPrice.toString())
                itemView.category_price_text_view.text = str
            } else {
                val str = itemView.context.resources.getText(R.string.us_dollar).toString().plus(gadget.salePrice.toString())
                itemView.category_price_text_view.text = str
            }


*/

            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if(gadget!!.currentBatch!=null) {
                if(gadget.offer_available!!){
                    itemView.product_original_price_text_view.visibility = View.VISIBLE
                    itemView.product_original_price_text_view.paintFlags =
                        itemView.product_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    val str = itemView.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(gadget!!.currentBatch!!.product_mrp))
                    itemView.product_original_price_text_view.text = str
                    if(gadget!!.offer_discount_price!=null){
                        itemView.product_sale_price_text_view.text =
                            "\u20B9" + df.format(gadget!!.offer_discount_price)
                    }else{
                        itemView.product_sale_price_text_view.text =
                            "\u20B9" + df.format(gadget.currentBatch!!.base_rate)
                    }

                }else {
                    if (gadget!!.currentBatch?.product_mrp == gadget!!.currentBatch?.base_rate!!) {

                        itemView.product_original_price_text_view.visibility = View.INVISIBLE
                        itemView.product_sale_price_text_view.text =
                            "\u20B9" + df.format(gadget!!.currentBatch!!.product_mrp)

                    } else {
                        itemView.product_original_price_text_view.visibility = View.VISIBLE
                        itemView.product_original_price_text_view.paintFlags =
                            itemView.product_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(gadget!!.currentBatch!!.product_mrp))
                        itemView.product_original_price_text_view.text = str
                        itemView.product_sale_price_text_view.text =
                            "\u20B9" + df.format(gadget!!.currentBatch!!.base_rate)


                    }
                }
            }

            itemView.product_name_text_view.text = gadget?.product_title

            val favDrawable: Drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.fav_enable)!!
            val removeFavDrawable: Drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.fav_disable)!!


            Log.e("WishList ::: ", "" + gadget?.wishList)
            if (gadget?.wishList != null) {
                gadget.isAddedToWishList = true
                itemView.add_to_wishlist_image_view.setImageDrawable(favDrawable)
            } else {
                gadget?.isAddedToWishList = false
                itemView.add_to_wishlist_image_view.setImageDrawable(removeFavDrawable)
            }
            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(
                    "" + gadget?.productId,
                    "",
                            gadget?.variationId
                )
            }

            itemView.add_to_wishlist_image_view?.setOnClickListener {
                val wishlist = gadget?.wishList
                Log.e("WishList :: ", "OnClick")
                if (wishlist != null) {
                    Log.e("WishList :: ", "not null")
                    gadget.isAddedToWishList = false
                    itemView.add_to_wishlist_image_view.setImageDrawable(
                        removeFavDrawable
                    )
                    adapterListener.onAdapterItemTapped(
                        "" + wishlist.wishList_id,
                        "removedFromWishList",
                                wishlist.variation_id

                    )
                } else {
                    Log.e("WishList :: ", "null")
                    gadget?.isAddedToWishList = true
                    itemView.add_to_wishlist_image_view.setImageDrawable(favDrawable)
                    adapterListener.onAdapterItemTapped(
                        "" + gadget?.variationId,
                        "addedToWishList",
                        gadget?.variationId

                    )
                }
            }


            Log.e("WishList ::: ", "" + gadget?.wishList)
            if (gadget?.wishList != null) {
//                gadget.isAddedToWishList = true
                itemView.add_to_wishlist_image_view.setImageDrawable(favDrawable)
            } else {
//                gadget.isAddedToWishList = false
                itemView.add_to_wishlist_image_view.setImageDrawable(removeFavDrawable)
            }

            itemView.add_to_wishlist_image_view?.setOnClickListener {
                val wishlist = gadget?.wishList
                Log.e("WishList :: ", "OnClick")
                if (wishlist != null) {
                    Log.e("WishList :: ", "not null")
//                    productsDataItems.isAddedToWishList = false
                    itemView.add_to_wishlist_image_view.setImageDrawable(
                        removeFavDrawable
                    )
                    adapterListener.onAdapterItemTapped(
                        "" + wishlist.wishList_id,
                        "removedFromWishList",
                        wishlist.variation_id
                    )
                } else {
                    Log.e("WishList :: ", "null")
//                    productsDataItems.isAddedToWishList = true
                    itemView.add_to_wishlist_image_view.setImageDrawable(favDrawable)
                    adapterListener.onAdapterItemTapped(
                        "" + gadget?.variationId,
                        "addedToWishList",gadget?.variationId
                    )
                }
            }
            if(gadget?.rating!=null){
                var avg=gadget?.rating

                itemView.ratingBar.rating=avg!!.toFloat()


            }
            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(gadget?.productId, "Product",gadget?.variationId)
            }
        }
    }
}