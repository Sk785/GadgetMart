package com.gadgetmart.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadgetmart.R
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import kotlinx.android.synthetic.main.dashboard_popular_gadget_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class PopularGadgetsAdapter(
    val context: Context?,
    private var gadgets: ArrayList<ProductVariation?>?,
    private val adapterListener: DashboardAdapterListener,
private val categoryName:String?

) :
    RecyclerView.Adapter<PopularGadgetsAdapter.PopularGadgetsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularGadgetsViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.dashboard_popular_gadget_item, parent, false)

        return PopularGadgetsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (gadgets == null || gadgets?.size == 0)
            0
        else if (gadgets?.size!! > 4) {
            4
        } else {
            gadgets!!.size
        }
    }

    override fun onBindViewHolder(holder: PopularGadgetsViewHolder, position: Int) {
        holder.bindView(gadgets!![position], adapterListener,categoryName!!,gadgets?.size!!)
        holder.adapterPosition
        gadgets?.size
    }

    fun updateList(gadgets: ArrayList<ProductVariation?>?) {
        this.gadgets = gadgets
        notifyDataSetChanged()
    }

    class PopularGadgetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            productVariation: ProductVariation?,
            adapterListener: DashboardAdapterListener,
            categoryName:String,
        size:Int
        ) {

//            if(size==1){
//                itemView.backView.setBackgroundResource(R.drawable.disable_home_item_back)
//                itemView.homeItemback.setBackgroundResource(R.drawable.disable_main_layout_view)
//            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if (!productVariation!!.productImages.isNullOrEmpty()) {
                if (productVariation!!.productImages?.get(0)?.name != null && productVariation!!.productImages?.get(0)?.name != "") {
                    Glide.with(itemView.context)
                        .load(productVariation!!.productImages?.get(0)?.name)
                        .placeholder(R.drawable.default_icon)
                        .into(itemView.popular_gadget_image_view)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(itemView.popular_gadget_image_view)
                }
            }

            itemView.popular_gadget_name_text_view.text = productVariation.product_title

            if (productVariation.currentBatch!= null) {
                if(productVariation.offer_available!!){
                    val str = itemView.context.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(productVariation.offer_discount_price))
                    itemView.popular_gadget_price_text_view.text = str
                }else{
                    val str = itemView.context.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(productVariation.currentBatch.base_rate))
                    itemView.popular_gadget_price_text_view.text = str
                }

            }

            itemView.setOnClickListener {
                adapterListener.onAdapterItemTapped(productVariation.productId, categoryName,productVariation.variationId)
            }

            if(productVariation.rating!=null){
                var avg=productVariation.rating.toFloat();

                itemView.ratingBar.rating=avg


            }

        }
    }
}