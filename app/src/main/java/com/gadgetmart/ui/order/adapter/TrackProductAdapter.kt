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
import com.gadgetmart.ui.order.model.TrackModelArray
import com.gadgetmart.util.DateUtils

class TrackProductAdapter (
    val context: Context?,
    private var productsListing: ArrayList<TrackModelArray>?
) :
    RecyclerView.Adapter<TrackProductAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.track_list_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsListing!!.size

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(context,productsListing!![position], position)
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context?,
            productsDataItems: TrackModelArray,
            position: Int
        ) {
            var status = itemView.findViewById(R.id.status) as TextView
            var remark = itemView.findViewById(R.id.remark) as TextView
            var date = itemView.findViewById(R.id.date) as TextView

            status.setText(productsDataItems.status)
            remark.setText(productsDataItems.remark)
            date.setText(productsDataItems.status_date?.let { DateUtils.convertStringDate(it) })

        }
    }

}
