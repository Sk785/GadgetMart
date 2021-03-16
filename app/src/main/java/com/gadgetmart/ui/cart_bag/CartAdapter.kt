package com.gadgetmart.ui.cart_bag

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.cart_bag.model.MyCart
import com.gadgetmart.ui.product_details.OfferListingActivity
import com.gadgetmart.ui.support.TermsAndCondtions
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.layout_increase_decrease_count.view.*
import java.math.RoundingMode
import java.text.DecimalFormat


class CartAdapter(
    val context: Context,
    var cartListing: ArrayList<MyCart>,
    var callback: CartInterface
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, cartListing, context, callback)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cartListing.size
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            position: Int,
            cartListing: ArrayList<MyCart>,
            context: Context,
            callback: CartInterface
        ) {
            val product_name = itemView.findViewById(R.id.product_name_text_view) as TextView
            val category_sale_price_text_view =
                itemView.findViewById(R.id.category_sale_price_text_view) as TextView
            val category_price_text_view =
                itemView.findViewById(R.id.category_original_price_text_view) as TextView
            val remove_txt = itemView.findViewById(R.id.remove_text_view) as TextView
            val product_img = itemView.findViewById(R.id.product_image_view) as ImageView
            val updateCartCountLayout = itemView.findViewById(R.id.update_count_layout) as LinearLayout
            val header_content=itemView.findViewById(R.id.header_content) as RelativeLayout
            val offer_info = itemView.findViewById(R.id.offer_info) as TextView

//            val plus_img = itemView.findViewById(R.id.increase_count_image_view) as ImageView
//            val minus_img = itemView.findViewById(R.id.decrease_count_image_view) as ImageView


            product_name.text = cartListing.get(position).variation?.product_title
            updateCartCountLayout.quantity_text_view.text = "" + cartListing.get(position).quantity

            if (cartListing.get(position).variation?.productImages?.get(0)?.name != null
                && cartListing[position].variation?.productImages?.get(0)?.name != ""
            ) {
                Glide.with(itemView.context)
                    .apply {
                        RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.default_icon)
                            .override(130, 100)
                            .fitCenter()
                    }
                    .load(cartListing.get(position).variation?.productImages?.get(0)?.name)
                    .placeholder(R.drawable.default_icon)
                    .into(product_img)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .into(product_img)
            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)

            if(cartListing[position].variation?.currentBatch != null)
            {
                if(cartListing[position].variation?.offer_available!!){
                    category_price_text_view.visibility = View.VISIBLE

                    category_price_text_view.paintFlags =
                        itemView.category_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = "\u20B9"
                        .plus(df.format(cartListing.get(position).variation?.currentBatch!!.product_mrp))

                    category_price_text_view.text = str
                    category_sale_price_text_view.text =
                        "\u20B9" + df.format(cartListing.get(position).variation?.offer_discount_price)
                }else {
                    if (cartListing[position].variation?.currentBatch!!.product_mrp ==
                        cartListing[position].variation?.currentBatch!!.base_rate!!

                    ) {
                        category_sale_price_text_view.text =
                            "\u20B9" + df.format(cartListing.get(position).variation?.currentBatch!!.product_mrp)

                        category_price_text_view.visibility = View.INVISIBLE
                    } else {
                        category_price_text_view.visibility = View.VISIBLE

                        category_price_text_view.paintFlags =
                            itemView.category_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = "\u20B9"
                            .plus(df.format(cartListing.get(position).variation?.currentBatch!!.product_mrp))

                        category_price_text_view.text = str
                        category_sale_price_text_view.text =
                            "\u20B9" + df.format(cartListing.get(position).variation?.currentBatch!!.base_rate)
                    }
                }
                header_content.setOnClickListener {
                    callback.clickOnView(position)

                }
                updateCartCountLayout.visibility=View.VISIBLE

            }else{
                header_content.setOnClickListener {
                    Toast.makeText(context,"Product sold out",Toast.LENGTH_SHORT).show()

                }
                category_sale_price_text_view.text="Product sold out"
                updateCartCountLayout.visibility=View.GONE
            }


            remove_txt.setOnClickListener {
                callback.clickOnItem(position)

            }


            updateCartCountLayout.increase_count_image_view.setOnClickListener {
                if(cartListing[position].variation!!.currentBatch!=null) {
                    if ((updateCartCountLayout.quantity_text_view.text.toString())
                        == cartListing[position].variation!!.currentBatch!!.quantity!!
                    ) {
                        Toast.makeText(context, "Out of stock product", Toast.LENGTH_SHORT).show()
                    } else {
                        var count =
                            (updateCartCountLayout.quantity_text_view.text.toString()).toInt()

                        count += 1
                        updateCartCountLayout.quantity_text_view.text = "" + count
                        cartListing[position].quantity = count
                        callback.addMoreProduct(
                            position,
                            cartListing[position].variation?.variationId!!,
                            cartListing[position].variation?.currentBatch!!.base_rate.toString(),
                            count
                        )

                    }
                }else{
                    Toast.makeText(context,"Out of stock product",Toast.LENGTH_SHORT).show()
                }

            }
            updateCartCountLayout.decrease_count_image_view.setOnClickListener {

                    if ((updateCartCountLayout.quantity_text_view.text.toString()).toInt() == 1) {
                    } else {
                        var count =
                            (updateCartCountLayout.quantity_text_view.text.toString()).toInt()

                        count = count - 1
                        updateCartCountLayout.quantity_text_view.text = "" + count

                        cartListing.get(position).quantity = count
                        callback.addLessProduct(
                            position,
                            cartListing[position].variation?.variationId!!,
                            cartListing.get(position).variation?.currentBatch!!.base_rate.toString(),
                            count
                        )


                    }


            }
            if(cartListing.get(position).variation?.offer_available!!){
                offer_info.visibility=View.VISIBLE
                offer_info.setText(cartListing.get(position).variation?.offers!![0].name)
                offer_info.setOnClickListener {
                    val i = Intent(context, TermsAndCondtions::class.java)
                    i.putExtra("type", cartListing.get(position).variation?.offers!![0].name)
                    i.putExtra("data", cartListing.get(position).variation?.offers!![0].terms)

                    context.startActivity(i)
                }
            }else{
                offer_info.visibility=View.GONE

            }


        }


    }

}