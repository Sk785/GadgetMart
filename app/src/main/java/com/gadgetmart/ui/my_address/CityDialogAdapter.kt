package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.gadgetmart.R


class CityDialogAdapter(
    context: Context,
    var cities: ArrayList<City>?,
    var adapterListener: DialogAdapterListener
) :
    ArrayAdapter<City>(context, R.layout.dialog_list_layout_item, cities!!) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.dialog_list_layout_item, parent, false)
        val textView = rowView.findViewById<View>(R.id.item_name_text_view) as TextView
        val radioButton: RadioButton = rowView.findViewById<View>(R.id.radio_button) as RadioButton
        textView.text = (cities?.get(position)?.name)

        rowView.setOnClickListener {

            radioButton.isChecked = !radioButton.isChecked
            cities?.get(position)?.name?.let { it1 ->
                cities?.get(position)?.id?.let { it2 ->
                    adapterListener.onAdapterItemClick(
                        "city",
                        it2,
                        it1
                    )
                }
            }
        }
        return rowView
    }

    fun updateList(cities: ArrayList<City>?) {
        this.cities = cities
        notifyDataSetChanged()
    }
}