package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.gadgetmart.R


class StateDialogAdapter(
    context: Context,
    var states: ArrayList<State>?,
    var adapterListener: DialogAdapterListener
) :
    ArrayAdapter<State>(context, R.layout.dialog_list_layout_item, states!!) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.dialog_list_layout_item, parent, false)
        val textView = rowView.findViewById<View>(R.id.item_name_text_view) as TextView
        val radioButton: RadioButton = rowView.findViewById<View>(R.id.radio_button) as RadioButton
        textView.text = (states?.get(position)?.name)

        rowView.setOnClickListener {

            radioButton.isChecked = !radioButton.isChecked
            states?.get(position)?.name?.let { it1 ->
                states?.get(position)?.id?.let { it2 ->
                    adapterListener.onAdapterItemClick(
                        "state",
                        it2,
                        it1
                    )
                }
            }
        }
        return rowView
    }

    fun updateList(states: ArrayList<State>?) {
        this.states = states
        notifyDataSetChanged()
    }
}