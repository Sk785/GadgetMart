package com.gadgetmart.ui.checkout

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.cart_bag.CartInterface
import com.gadgetmart.ui.cart_bag.model.MyCart
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.support.TermsAndCondtions
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.layout_increase_decrease_count.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class CheckOutDirectAdapter( val context: Context,
var cartListing: ProductVariation,
var callback: CartInterface,var count:Int
) :
RecyclerView.Adapter<CheckOutDirectAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.checkout_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, cartListing, context, callback,count)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return 1
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
                position: Int,
                cartListing: ProductVariation,
                context: Context,
                callback: CartInterface,
        count:Int
        ) {
            //  val categoryName = itemView.findViewById(R.id.category_name_text_view) as TextView
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


            product_name.text = cartListing.product_title
            updateCartCountLayout.quantity_text_view.text = "" + count

            if (cartListing?.productImages?.get(0)?.name != null
                    && cartListing?.productImages?.get(0)?.name != ""
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
                        .load(cartListing?.productImages?.get(0)?.name)
                        .placeholder(R.drawable.default_icon)
                        .into(product_img)
            } else {
                Glide.with(itemView.context)
                        .load(R.drawable.default_icon)
                        .into(product_img)
            }
            val df = DecimalFormat("#.##")
            df.setRoundingMode(RoundingMode.CEILING)
            if(cartListing?.currentBatch!=null) {
                if(cartListing?.offer_available!!){
                    category_price_text_view.visibility = View.VISIBLE

                    category_price_text_view.paintFlags =
                        itemView.category_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = "\u20B9"
                        .plus(df.format(cartListing?.currentBatch!!.product_mrp))

                    category_price_text_view.text = str
                    category_sale_price_text_view.text =
                        "\u20B9" + df.format(cartListing?.offer_discount_price!!.toFloat())
                }else {
                    if (cartListing?.currentBatch!!.product_mrp ==
                        cartListing?.currentBatch!!.base_rate!!

                    ) {
                        category_sale_price_text_view.text =
                            "\u20B9" + df.format(cartListing.currentBatch!!.product_mrp)

                        category_price_text_view.visibility = View.INVISIBLE
                    } else {
                        category_price_text_view.visibility = View.VISIBLE

                        category_price_text_view.paintFlags =
                            itemView.category_original_price_text_view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        val str = "\u20B9"
                            .plus(df.format(cartListing?.currentBatch!!.product_mrp))

                        category_price_text_view.text = str
                        category_sale_price_text_view.text =
                            "\u20B9" + df.format(cartListing?.currentBatch!!.base_rate)
                    }
                }
                header_content.setOnClickListener {
                    callback.clickOnView(position)

                }
                updateCartCountLayout.visibility=View.VISIBLE

            }else{
                header_content.setOnClickListener {
                    callback.clickOnView(position)
                    Toast.makeText(context,"Product sold out",Toast.LENGTH_SHORT).show()

                }
                category_sale_price_text_view.text="Product sold out"
                updateCartCountLayout.visibility=View.GONE

            }

            remove_txt.visibility=View.GONE


            updateCartCountLayout.increase_count_image_view.setOnClickListener {
                if(cartListing!!.currentBatch!=null) {
                    if ((updateCartCountLayout.quantity_text_view.text.toString())
                        == cartListing!!.currentBatch!!.quantity!!
                    ) {
                        Toast.makeText(context, "Out of stock product", Toast.LENGTH_SHORT).show()
                    } else {

                        callback.addMoreProduct(
                            position,
                            cartListing?.variationId!!,
                            cartListing?.currentBatch!!.base_rate.toString(),
                            count
                        )

                    }
                }else{
                    Toast.makeText(context, "Out of stock product", Toast.LENGTH_SHORT).show()

                }

            }
            updateCartCountLayout.decrease_count_image_view.setOnClickListener {

                    if ((updateCartCountLayout.quantity_text_view.text.toString()).toInt() == 1) {
                    } else {

                        callback.addLessProduct(
                            position,
                            cartListing?.variationId!!,
                            cartListing?.currentBatch!!.base_rate.toString(),
                            count
                        )


                    }


            }
            if(cartListing?.offer_available!!){
                offer_info.visibility= View.VISIBLE
                offer_info.setText(cartListing?.offers!![0].name)
                offer_info.setOnClickListener {
                    val i = Intent(context, TermsAndCondtions::class.java)
                    i.putExtra("type", cartListing?.offers!![0].name)
                    i.putExtra("data", cartListing?.offers!![0].terms)

                    context.startActivity(i)
                }
            }else{
                offer_info.visibility= View.GONE

            }

        }


    }



}