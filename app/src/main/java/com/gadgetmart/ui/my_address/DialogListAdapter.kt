package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.dialog_list_layout_item.view.*

class DialogListAdapter(
    val context: Context?,
    var countries: ArrayList<Country>?,
    private val adapterListener: DialogAdapterListener
) :
    RecyclerView.Adapter<DialogListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_list_layout_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (countries == null)
            0
        else
            countries.let { it!!.size }
    }

    fun notifyList(countries: ArrayList<Country>){
        this.countries=countries
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(countries!![position], adapterListener)
    }

    fun updateList(countries: ArrayList<Country>?) {
        this.countries = countries
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            country: Country?,
            adapterListener: DialogAdapterListener
        ) {
            itemView.item_name_text_view?.text = (country?.name)

            itemView.setOnClickListener {

                itemView.radio_button?.isChecked = !(itemView.radio_button)?.isChecked!!
                country?.name?.let { it1 ->
                    country?.id?.let { it2 ->
                        adapterListener.onAdapterItemClick(
                            "country",
                            it2,
                            it1
                        )
                    }
                }
            }
        }
    }
}
