package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R


class CountryDialogAdapter(
    context: Context,
    var countries: ArrayList<Country>?,
    var adapterListener: DialogAdapterListener
) :
    ArrayAdapter<Country>(context, R.layout.dialog_list_layout_item, countries!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: CustomViewHolder
        val retView: View

        if (convertView == null) {

            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            retView = inflater.inflate(R.layout.dialog_list_layout_item, null)
            holder = CustomViewHolder(retView)

            holder.textView = retView.findViewById(R.id.item_name_text_view) as TextView?

            retView.tag = holder
            holder.textView?.text = (countries?.get(position)?.name)

            holder.itemView.setOnClickListener {

                holder.radioButton?.isChecked = !(holder.radioButton)?.isChecked!!
                countries?.get(position)?.name?.let { it1 ->
                    countries?.get(position)?.id?.let { it2 ->
                        adapterListener.onAdapterItemClick(
                            "country",
                            it2,
                            it1
                        )
                    }
                }
            }

        } else {
            holder = convertView.tag as CustomViewHolder
            retView = convertView
        }
        return retView

//        val inflater = context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val rowView: View = inflater.inflate(R.layout.dialog_list_layout_item, parent, false)
//        val textView = rowView.findViewById<View>(R.id.item_name_text_view) as TextView
//        val radioButton: RadioButton = rowView.findViewById<View>(R.id.radio_button) as RadioButton
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var radioButton: RadioButton? = null
        var textView: TextView? = null
    }

    fun updateList(countries: ArrayList<Country>?) {
        this.countries = countries
        notifyDataSetChanged()
    }


    @Nullable
    override fun getItem(position: Int): Country? {
        return countries?.get(position)
    }

    override fun getCount(): Int {
        return countries?.size!!
    }
}