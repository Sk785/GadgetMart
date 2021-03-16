package com.gadgetmart.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.order.interfaces.MyOrderInterface

class CancelOrderListAdapter(
    val context: Context?, var reasonList: ArrayList<HashMap<String, String>>,
    private var dashboardAdapterListener: MyOrderInterface
) : RecyclerView.Adapter<CancelOrderListAdapter.ViewHolder>() {
    var viewHolder : ViewHolder? = null
    var selected : RadioButton? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_layout_cancel_order_list, parent, false);
        viewHolder = ViewHolder(view)
        return viewHolder as ViewHolder
    }

    override fun getItemCount(): Int {
        return reasonList!!.size
    }

    public fun notifyData(reasonList: ArrayList<HashMap<String, String>>){
        this.reasonList = reasonList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var tvReason = holder.itemView.findViewById(R.id.tvReason) as TextView
        var rbButton = holder.itemView.findViewById(R.id.rbButton) as RadioButton
        var clItemLayout = holder.itemView.findViewById(R.id.clItemLayout) as ConstraintLayout

        clItemLayout.setOnClickListener{
            if (selected != null){
                selected!!.isSelected = false
            }
            dashboardAdapterListener.onItemClicked(position)
            rbButton.isSelected = true
            selected = rbButton
        }
        rbButton.setOnClickListener{
            if (selected != null){
                selected!!.isSelected = false
            }
            dashboardAdapterListener.onItemClicked(position)
            rbButton.isSelected = true
        }
        tvReason.setText(reasonList.get(position).get("reason"))
        if (reasonList.get(position).get("isSelected").equals("false")){
            rbButton.isChecked = false
        }else {
            rbButton.isChecked = true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var selected: RadioButton? = null
        fun bindView(
            context: Context?,
            reasonList: ArrayList<String>,
            position: Int,
            dashboardAdapterListener: MyOrderInterface
        ) {



        }
    }
}