package com.gadgetmart.ui.category

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
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import kotlinx.android.synthetic.main.dashboard_category_item.view.category_image_view
import kotlinx.android.synthetic.main.dashboard_category_item.view.category_name_text_view
import kotlinx.android.synthetic.main.dashboard_offer_item.view.*
import kotlinx.android.synthetic.main.dashboard_popular_gadget_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class OffersAdapter(
    val context: Context?,
    private var gadgets: ArrayList<ProductVariation?>?,
    private val adapterListener: DashboardAdapterListener,
private val categoryName:String?
) :
    RecyclerView.Adapter<OffersAdapter.OffersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.dashboard_offer_item, parent, false)

        return OffersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (gadgets?.size == 0)
            0
        else {
            gadgets!!.size
        }
    }

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        holder.bindView(gadgets!![position], adapterListener,categoryName!!)
    }

    fun updateList(gadgets: ArrayList<ProductVariation?>?) {
        this.gadgets = gadgets
        notifyDataSetChanged()
    }

    class OffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            gadget: ProductVariation?,
            adapterListener: DashboardAdapterListener,
            categoryName:String
        ) {
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if(gadget?.variation!=null){


                if (!gadget?.variation!!.productImages.isNullOrEmpty()) {
                    if (gadget?.variation!!.productImages?.get(0)?.name != null && gadget?.variation!!.productImages?.get(
                            0
                        )?.name != ""
                    ) {
                        Glide.with(itemView.context)
//                            .apply {
//                                RequestOptions()
//                                    .fitCenter()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .placeholder(R.drawable.default_icon)
//                                    .override(256, 140)
//                                    .fitCenter()
                            .load(gadget.variation!!.productImages?.get(0)?.name)
                            .placeholder(R.drawable.default_icon)
                            .into(itemView.category_image_view)
                    } else {
                        Glide.with(itemView.context)
                            .load(R.drawable.default_icon)
                            .into(itemView.category_image_view)
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
                if (gadget?.variation!!.currentBatch != null) {
                    if(gadget.variation.offer_available!!){
                        itemView.category_price_text_view.visibility = View.VISIBLE
                        itemView.category_price_text_view.paintFlags =
                            itemView.category_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(gadget?.variation!!.currentBatch?.product_mrp))
                        itemView.category_price_text_view.text = str
                        itemView.category_sale_price_text_view.text =
                            "\u20B9" + df.format(gadget?.variation.offer_discount_price)
                    }else {
                        if (gadget?.variation!!.currentBatch?.product_mrp == gadget?.variation!!.currentBatch?.base_rate!!) {
                            itemView.category_price_text_view.visibility = View.INVISIBLE
                            itemView.category_sale_price_text_view.visibility = View.VISIBLE
                            itemView.category_sale_price_text_view.text =
                                "\u20B9" + df.format(gadget?.variation!!.currentBatch?.product_mrp)


                        } else {
                            itemView.category_price_text_view.visibility = View.VISIBLE
                            itemView.category_price_text_view.paintFlags =
                                itemView.category_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                            val str = itemView.resources.getText(R.string.us_dollar).toString()
                                .plus(df.format(gadget?.variation!!.currentBatch?.product_mrp))
                            itemView.category_price_text_view.text = str
                            itemView.category_sale_price_text_view.text =
                                "\u20B9" + df.format(gadget?.variation!!.currentBatch?.base_rate)

                        }
                    }
                }
                itemView.category_name_text_view.text = gadget?.variation!!.product?.name
                itemView.setOnClickListener {
                    adapterListener.onAdapterItemTapped(
                        gadget.variation!!.productId,
                        categoryName,
                        gadget.variation!!.variationId
                    )
                }
            }
        }
    }
}