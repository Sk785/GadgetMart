package com.gadgetmart.ui.product_details

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.varient_bottom_view.view.*
import kotlinx.android.synthetic.main.varient_item.view.*

class BottomVaiationSubListAdapter (
    val context: Context?,
    var parentPos:Int,
    private var variations: ArrayList<SubListModel>?,
    private var dashboardAdapterListener: SubCategoryInterVariant
) :
    RecyclerView.Adapter<BottomVaiationSubListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.varient_sub_list_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variations?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(context!!,dashboardAdapterListener, variations!![position], parentPos)
    }

    fun updateList(variations: ArrayList<SubListModel>?) {
        this.variations = variations
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context,
            dashboardAdapterListener: SubCategoryInterVariant,
            variation: SubListModel,
            p: Int
        ) {
            itemView.variation_name.setText(variation.variation_name)

            if(variation.isSelected==true){
                itemView.variation_name.setBackgroundResource(R.drawable.background_radial)
                itemView.variation_name.setTextColor(context.resources.getColor(R.color.white))

            }else{
                itemView.variation_name.setBackgroundResource(R.drawable.unselected_back)
                itemView.variation_name.setTextColor(context.resources.getColor(R.color.gray))

            }
            itemView.variation_name.setOnClickListener {
                dashboardAdapterListener.onClickSubVarientItem(p,itemView.variation_name.text.toString(),true)
            }


        }
    }

}
