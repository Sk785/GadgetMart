package com.gadgetmart.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.gadgetmart.ui.order.model.DataModelArray

class MyOrderAdapter(
    val context: Context?,
    private var productsListing: ArrayList<DataModelArray>?,
    private var dashboardAdapterListener: MyOrderInterface

    ) :
    RecyclerView.Adapter<MyOrderAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.myorder_list_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsListing!!.size

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(context, dashboardAdapterListener, productsListing!![position], position)
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context?,
            dashboardAdapterListener: MyOrderInterface,
            productsDataItems: DataModelArray,
            position: Int
        ) {
            var order_txt = itemView.findViewById(R.id.order_id_txt) as TextView
            var order_details_txt = itemView.findViewById(R.id.order_details_txt) as TextView
            var order_sub_item = itemView.findViewById(R.id.order_sub_item) as RecyclerView
            order_sub_item.layoutManager = LinearLayoutManager(context)
            order_sub_item.adapter = MyOrderSubItemAdapter(
                context,
                productsDataItems.order_products,

                dashboardAdapterListener,
                position,
                productsDataItems.payment_status!!
            )

            order_txt.setText("Id: #"+productsDataItems.order_number)

            if(productsDataItems.payment_status.equals("Done")){
                order_details_txt.setTextColor(context!!.resources.getColor(R.color.button_green))
                order_details_txt.setText("Paid")

            }else  if(productsDataItems.payment_status.equals("Failed")){
                order_details_txt.setTextColor(context!!.resources.getColor(R.color.colorPrimary))
                order_details_txt.setText(productsDataItems.payment_status)

            }else{
                order_details_txt.setTextColor(context!!.resources.getColor(R.color.gray))
                order_details_txt.setText(productsDataItems.payment_status)

            }
            order_details_txt.setOnClickListener {
                //dashboardAdapterListener.parentClick(position)
            }
        }
    }

    fun updateList(productsListing: ArrayList<DataModelArray>?) {
        this.productsListing = productsListing
        notifyDataSetChanged()
    }
}