package com.gadgetmart.ui.wishlist

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.products_of_sub_category.ProductDataWishlist
import kotlinx.android.synthetic.main.gadgets_bag_items_layout.view.*
import kotlinx.android.synthetic.main.products_of_subcategory_items_layout.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MyWishListAdapter(
    val context: Context?,
    private var wishList: ArrayList<ProductDataWishlist>?,
    private val adapterListener: WishListAdapterListener
) :
    RecyclerView.Adapter<MyWishListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.gadgets_bag_items_layout, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (wishList == null)
            0
        else
            wishList.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(wishList!![position], adapterListener, position)
    }

    fun updateList(wishList: ArrayList<ProductDataWishlist>?) {

        this.wishList = wishList
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            wishListItems: ProductDataWishlist?,
            adapterListener: WishListAdapterListener,
            position: Int
        ) {
            itemView.add_to_bag_item?.visibility = View.VISIBLE
            val wishListProduct = wishListItems?.variation
            if (wishListProduct != null){
                if (wishListProduct.productImages != null && wishListProduct.productImages.isNotEmpty()){
                    Glide.with(itemView.context)
                        .apply { RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.default_icon)
                            .override(256, 140)
                            .fitCenter(); }
                        .load(wishListProduct.productImages[0].name)
                        .placeholder(R.drawable.default_icon)
                        .into(itemView.bag_image_view)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(itemView.bag_image_view)
                }
            }

            itemView.bag_title_text_view.text = wishListProduct?.product_title
            itemView.bag_item_detail_text_view.text = wishListProduct?.short_description

            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if(wishListProduct?.currentBatch!=null) {
if(wishListProduct?.offer_available!!){
    itemView.bag_item_net_price_text_view.visibility = View.VISIBLE
    itemView.bag_item_net_price_text_view.paintFlags =
        itemView.bag_item_net_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    itemView.bag_item_net_price_text_view.text =
        "\u20B9" + df.format(wishListProduct?.currentBatch?.product_mrp!!)
    itemView.bag_item_price_text_view.text =
        "\u20B9" + df.format(wishListProduct?.offer_discount_price!!)
}else {

    if (wishListProduct?.currentBatch?.base_rate!!.equals(wishListProduct?.currentBatch?.product_mrp!!)) {
        itemView.bag_item_net_price_text_view.visibility = View.GONE
        itemView.bag_item_price_text_view.text =
            "\u20B9" + df.format(wishListProduct?.currentBatch?.product_mrp!!)


    } else {
        itemView.bag_item_net_price_text_view.visibility = View.VISIBLE
        itemView.bag_item_net_price_text_view.paintFlags =
            itemView.bag_item_net_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        itemView.bag_item_net_price_text_view.text =
            "\u20B9" + df.format(wishListProduct?.currentBatch?.product_mrp!!)
        itemView.bag_item_price_text_view.text =
            "\u20B9" + df.format(wishListProduct?.currentBatch?.base_rate!!)
    }
}
            }
            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(wishListItems?.variation!!.productId!!.toInt(), "Product Details", wishListItems?.variation_id!!.toInt() )
            }

            itemView.bag_remove_item.setOnClickListener{
                adapterListener.onAdapterItemTapped(wishListItems?.wishList_id, "removedFromWishList", position )

            }

            itemView.add_to_bag_item.setOnClickListener{
                adapterListener.onAddToBag(
                    position,wishListItems!!.variation_id,""
                )
              //  adapterListener.onAdapterItemTapped(wishListItems?.variation_id!!.toInt(), "addToBag", position )

            }
        }


    }

}
