package com.gadgetmart.ui.product_details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.varient_bottom_list_item.view.*
import kotlinx.android.synthetic.main.varient_bottom_view.view.*
import kotlinx.android.synthetic.main.varient_item.view.*
import kotlinx.android.synthetic.main.varient_item.view.variation_name

class BottomVariationsAdapter (
    val context: Context?,
    private var variations: ArrayList<VarientModel>?,
    private var dashboardAdapterListener: SubCategoryInterVariant
) :
    RecyclerView.Adapter<BottomVariationsAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.varient_bottom_list_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variations?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(context!!,dashboardAdapterListener, variations!![position], position)
    }

    fun updateList(variations: ArrayList<VarientModel>?) {
        this.variations = variations
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            context: Context,
            dashboardAdapterListener: SubCategoryInterVariant,
            variation: VarientModel,
            p: Int
        ) {
            itemView.variation_name.setText(variation.name)


            val subCategoriesLayoutManager = LinearLayoutManager(context)

            subCategoriesLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

            itemView.variation_list.layoutManager = subCategoriesLayoutManager
            var result = variation.value!!.split(",").map { it.trim() }
            var subListModel:ArrayList<SubListModel>? = ArrayList()
            Log.e("variation!!.selectdText",variation!!.selectdText);
            for(i in 0 until result.size){

                    if(variation!!.selectdText==result[i]){
                        subListModel!!.add(SubListModel(result[i],true));

                    }else{
                        subListModel!!.add(SubListModel(result[i],false));

                    }



            }
            var bottomAdapter = BottomVaiationSubListAdapter(context,p,subListModel, dashboardAdapterListener)
            itemView.variation_list.adapter = bottomAdapter
        }
    }

}
