package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.dialog_list_layout_item.view.*

class CityDialogListAdapter(
    val context: Context?,
    var cities: ArrayList<City>?,
    private val adapterListener: DialogAdapterListener
) :
    RecyclerView.Adapter<CityDialogListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_list_layout_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (cities == null)
            0
        else
            cities.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(cities!![position], adapterListener)
    }

    fun updateList(cities: ArrayList<City>?) {
        this.cities = cities
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            city: City?,
            adapterListener: DialogAdapterListener
        ) {
            itemView.item_name_text_view?.text = (city?.name)

            itemView.setOnClickListener {

                itemView.radio_button?.isChecked = !(itemView.radio_button)?.isChecked!!
                city?.name?.let { it1 ->
                    city?.id?.let { it2 ->
                        adapterListener.onAdapterItemClick(
                            "city",
                            it2,
                            it1
                        )
                    }
                }
            }
        }
    }
}
