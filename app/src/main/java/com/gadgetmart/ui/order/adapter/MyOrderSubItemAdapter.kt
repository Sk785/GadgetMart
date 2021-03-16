package com.gadgetmart.ui.order.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.gadgetmart.ui.order.model.OrderProducts
import com.gadgetmart.util.DateUtils
import kotlinx.android.synthetic.main.myorder_sub_item.view.*
import kotlinx.android.synthetic.main.product_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MyOrderSubItemAdapter(
    val context: Context?,
    private var productsListing: ArrayList<OrderProducts>?,
    private var dashboardAdapterListener: MyOrderInterface, var parentPos: Int,var status:String
) :
    RecyclerView.Adapter<MyOrderSubItemAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.myorder_sub_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsListing!!.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(
            context,
            dashboardAdapterListener,
            productsListing!![position],
            position,
            parentPos,
            status
        )
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context?,
            dashboardAdapterListener: MyOrderInterface,
            productsDataItems: OrderProducts,
            position: Int, parentPos: Int,status:String
        ) {
            val products_of_subcategory_image_view =
                itemView.findViewById(R.id.products_of_subcategory_image_view) as ImageView
            val products_of_subcategory_price =
                itemView.findViewById(R.id.products_of_subcategory_price) as TextView
            val deliveryStatus = itemView.findViewById(R.id.tvStatus) as TextView
            val products_of_subcategory_discount =
                itemView.findViewById(R.id.products_of_subcategory_discount) as TextView
            val products_of_subcategory_text_name =
                itemView.findViewById(R.id.products_of_subcategory_text_name) as TextView
            val quantity_txt = itemView.findViewById(R.id.quantity_txt) as TextView
            val date_txt = itemView.findViewById(R.id.date_txt) as TextView
            if (!productsDataItems.product_data?.productImages.isNullOrEmpty()) {
                if (productsDataItems.product_data?.productImages?.get(0)?.name != null && productsDataItems.product_data?.productImages[0].name != "") {
                    Glide.with(itemView.context)
                        .apply {
                            RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_icon)
                                .override(256, 140)
                                .fitCenter();
                        }
                        .load(productsDataItems.product_data.productImages[0].name)
                        .placeholder(R.drawable.default_icon)
                        .into(products_of_subcategory_image_view)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(products_of_subcategory_image_view)
                }
            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if (productsDataItems.product_data?.currentBatch!!.base_rate!!.equals(productsDataItems.product_data.currentBatch.product_mrp!!)) {
                products_of_subcategory_price.text =
                    "\u20B9" +df.format(productsDataItems.product_data.currentBatch.product_mrp)

                products_of_subcategory_discount.visibility = View.GONE
            } else {
                if(productsDataItems.product_data?.offer_available==true){
                    products_of_subcategory_discount.paintFlags =
                        products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = itemView.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(productsDataItems.product_data.currentBatch.product_mrp))
                    products_of_subcategory_discount.text = str

                    if( productsDataItems.product_data.offer_discount_price!=null) {
                        products_of_subcategory_price.text =
                            "\u20B9" + df.format(productsDataItems.product_data.offer_discount_price)
                    }else{
                        products_of_subcategory_price.text =
                            "\u20B9" +df.format( productsDataItems.product_data.currentBatch.base_rate)
                    }
                }else{
                    products_of_subcategory_discount.paintFlags =
                        products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = itemView.resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(productsDataItems.product_data.currentBatch.product_mrp))
                    products_of_subcategory_discount.text = str
                    products_of_subcategory_price.text =
                        "\u20B9" +df.format( productsDataItems.product_data.currentBatch.base_rate)
                }

            }



            products_of_subcategory_text_name.text = productsDataItems.product_data.product_title
            itemView.setOnClickListener {
                dashboardAdapterListener.childClick(parentPos, position)
            }
            quantity_txt.setText("Qty:- " + productsDataItems.quantity)

            if(productsDataItems.status.equals("Pending"))
                date_txt.visibility = View.GONE
            else
                date_txt.visibility = View.VISIBLE

            if(productsDataItems.status.equals("Delivered"))
                itemView.rating.visibility=View.VISIBLE
            else
                itemView.rating.visibility=View.GONE

            date_txt.setText(productsDataItems.status_date?.let { DateUtils.convertStringDate(it) })
            deliveryStatus.setText(productsDataItems.status)

            if(status.equals("Failed")){
                deliveryStatus.setText("")
            }

            if(productsDataItems.rating!=null){
                var avg=productsDataItems.rating

                itemView.rating.rating=avg!!.toFloat()


            }
            itemView.setOnClickListener {
                dashboardAdapterListener.parentClick(parentPos)
            }
        }
    }
}