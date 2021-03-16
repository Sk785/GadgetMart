package com.gadgetmart.ui.order.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.order.TrackProduct
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.gadgetmart.ui.order.model.OrderProducts
import com.gadgetmart.util.DateUtils
import kotlinx.android.synthetic.main.myorder_sub_item.view.*
import kotlinx.android.synthetic.main.product_list_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class OrderDetailListAdapter (
    val context: Context?,
    private var productsListing: ArrayList<OrderProducts>?,
    private var dashboardAdapterListener: MyOrderInterface, var parentPos: Int,var isCancel:Boolean,var status:String
) :
    RecyclerView.Adapter<OrderDetailListAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.order_detail_list_item, parent, false)

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
            parentPos,isCancel,status
        )
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context?,
            dashboardAdapterListener: MyOrderInterface,
            productsDataItems: OrderProducts,
            position: Int, parentPos: Int,isCancel:Boolean,status:String
        ) {
            val products_of_subcategory_image_view =
                itemView.findViewById(R.id.products_of_subcategory_image_view) as ImageView
            val tvCancelOrder =
                itemView.findViewById(R.id.tvCancelOrder) as TextView
            val btnAddReview =
                itemView.findViewById(R.id.btnAddReview) as Button
            val btnReturnReplacement =
                itemView.findViewById(R.id.btnReturnReplacement) as Button
            val products_of_subcategory_price =
                itemView.findViewById(R.id.products_of_subcategory_price) as TextView
            val liAddReview =
                itemView.findViewById(R.id.liAddReview) as LinearLayout
            val deliveryStatus
                    = itemView.findViewById(R.id.tvStatus) as TextView
            val products_of_subcategory_discount =
                itemView.findViewById(R.id.products_of_subcategory_discount) as TextView
            val products_of_subcategory_text_name =
                itemView.findViewById(R.id.products_of_subcategory_text_name) as TextView
            var writeReview=itemView.findViewById(R.id.writeReview) as TextView
            val quantity_txt=itemView.findViewById(R.id.quantity_txt) as TextView
            val date_txt=itemView.findViewById(R.id.date_txt) as TextView
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
            if (productsDataItems.product_data!!.currentBatch!!.base_rate!!.equals(productsDataItems.product_data.currentBatch!!.product_mrp!!)) {
               if(productsDataItems.product_data?.offer_available!!){
                   products_of_subcategory_price.text =
                       "\u20B9" + df.format(productsDataItems.product_data?.offer_discount_price)
               }else{
                   products_of_subcategory_price.text =
                       "\u20B9" + df.format(productsDataItems.product_data?.currentBatch?.base_rate)
               }




                products_of_subcategory_discount.visibility = View.GONE
            } else {
                if (productsDataItems.product_data?.offer_available != null){
                    if (productsDataItems.product_data?.offer_available!!) {

                        products_of_subcategory_discount.paintFlags =
                            products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(productsDataItems.product_data.currentBatch.product_mrp))
                        products_of_subcategory_discount.text = str
                        if(productsDataItems.product_data.offer_discount_price!=null){
                            products_of_subcategory_price.text =
                                "\u20B9" + df.format(productsDataItems.product_data.offer_discount_price)
                        }else{
                            products_of_subcategory_price.text =
                                "\u20B9" + df.format(productsDataItems.product_data.currentBatch.base_rate)

                        }

                    } else {
                        products_of_subcategory_discount.paintFlags =
                            products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(productsDataItems.product_data.currentBatch.product_mrp))
                        products_of_subcategory_discount.text = str
                        products_of_subcategory_price.text =
                            "\u20B9" + df.format(productsDataItems.product_data.currentBatch.base_rate)
                    }
            }else{
                       products_of_subcategory_discount.paintFlags =
                            products_of_subcategory_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = itemView.resources.getText(R.string.us_dollar).toString()
                            .plus(df.format(productsDataItems.product_data.currentBatch.product_mrp))
                        products_of_subcategory_discount.text = str
                        products_of_subcategory_price.text =
                            "\u20B9" + df.format(productsDataItems.product_data.currentBatch.base_rate)
                }
            }

            products_of_subcategory_text_name.text = productsDataItems.product_data?.product_title
            itemView.setOnClickListener {
            }
            Log.e("status",productsDataItems.status)
            if (productsDataItems.status.equals("Cancelled")){
                tvCancelOrder.visibility = View.GONE
                writeReview.visibility = View.GONE

            }else if(productsDataItems.status.equals("Delivered"))
            {
                if(productsDataItems.is_product_refund){
                    liAddReview.visibility = View.VISIBLE
                    writeReview.visibility = View.GONE


                }else{
                    liAddReview.visibility = View.GONE
                    writeReview.visibility = View.VISIBLE


                }


                date_txt.visibility = View.VISIBLE
                writeReview.setOnClickListener {
                    dashboardAdapterListener.openReviewScreen(parentPos,position)
                }
            }else if ((productsDataItems.status == "Pending")
                    || (productsDataItems.status == "Processing")
                    || (productsDataItems.status == "Ready for Pickup")
                    || (productsDataItems.status == "Out for Delivery")
                    || (productsDataItems.status == "Shipped")
                    || (productsDataItems.status == "Acknowledged")){
                tvCancelOrder.visibility = View.VISIBLE
                date_txt.visibility = View.GONE
                writeReview.setText("Track Product")
                liAddReview.visibility = View.GONE
                writeReview.setTextColor(context!!.resources!!.getColor(R.color.colorPrimary))
                writeReview.setOnClickListener {
                    Log.e("product_id",productsDataItems.id)

                    var i=Intent(context,TrackProduct::class.java)
                    i.putExtra("product_id",productsDataItems.id)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(i)
                }

            }else{
                tvCancelOrder.visibility = View.GONE
                date_txt.visibility = View.VISIBLE
                writeReview.setText("Track Product")
                liAddReview.visibility = View.GONE
                writeReview.setTextColor(context!!.resources!!.getColor(R.color.colorPrimary))
                writeReview.setOnClickListener {
                    Log.e("product_id",productsDataItems.id)

                    var i=Intent(context,TrackProduct::class.java)
                    i.putExtra("product_id",productsDataItems.id)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(i)
                }
            }
            quantity_txt.setText("Qty:- "+productsDataItems.quantity)
//            if (productsDataItems.status.equals("Pending")) {
//                date_txt.visibility = View.GONE
//                writeReview.setText("Track Product")
//                liAddReview.visibility = View.GONE
//                writeReview.setTextColor(context!!.resources!!.getColor(R.color.colorPrimary))
//                writeReview.setOnClickListener {
//                    Log.e("product_id",productsDataItems.id)
//
//                    var i=Intent(context,TrackProduct::class.java)
//                    i.putExtra("product_id",productsDataItems.id)
//                    context.startActivity(i)
//                }
//
//            }
//            else {
//                writeReview.visibility = View.GONE
//                if (productsDataItems.status.equals("Delivered")){
//                    liAddReview.visibility = View.VISIBLE
//                }
//
//                date_txt.visibility = View.VISIBLE
//                writeReview.setOnClickListener {
//                    dashboardAdapterListener.openReviewScreen(parentPos,position)
//                }
//
//            }
            deliveryStatus.setText(productsDataItems.status)
            date_txt.setText(productsDataItems.status_date?.let { DateUtils.convertStringDate(it) })
            btnAddReview.setOnClickListener{
                dashboardAdapterListener.openReviewScreen(parentPos,position)
            }

            tvCancelOrder.setOnClickListener{
                if(isCancel){
                    dashboardAdapterListener.openCancelScreen(parentPos, position, 0)
                    tvCancelOrder.setBackgroundResource(R.drawable.background_round_left_stroke_red)


                }else {
                    Toast.makeText(context,"You can't cancel order",Toast.LENGTH_SHORT).show()
                    tvCancelOrder.setBackgroundResource(R.drawable.background_round_stroke_grey)
                }
            }
            if(isCancel){
                tvCancelOrder.setBackgroundResource(R.drawable.background_round_left_stroke_red)


            }else {
                tvCancelOrder.setBackgroundResource(R.drawable.background_round_stroke_grey)
            }

            btnReturnReplacement.setOnClickListener {
                dashboardAdapterListener.openCancelScreen(parentPos,position,1)

            }
            if(productsDataItems.rating!=null){
                var avg=productsDataItems.rating

                itemView.rating.rating=avg!!.toFloat()
                if(productsDataItems.status.equals("Delivered")){
                    btnAddReview.visibility=View.INVISIBLE
                    if(!productsDataItems.is_product_refund){
                        liAddReview.visibility = View.GONE

                    }

                }

            }

            if(status.equals("Failed")){
                tvCancelOrder.visibility = View.GONE
                writeReview.visibility = View.GONE
                deliveryStatus.setText("Failed")

            }

        }
    }
}