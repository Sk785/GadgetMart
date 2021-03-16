package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.my_address.AddAddressItems
import com.gadgetmart.ui.subcategory.AddressAdapterListener
import kotlinx.android.synthetic.main.address_bottom_sheet_item.view.*
import kotlinx.android.synthetic.main.my_addresses_activity_item.view.*

class SavedAddressesAdapter(
    val context: Context?,
    private var addresses: ArrayList<AddAddressItems>?,
    private var adapterListener: AddressAdapterListener
) :
    RecyclerView.Adapter<SavedAddressesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.address_bottom_sheet_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (addresses == null) 0
        else addresses.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.itemView.name_pin_text_view?.text = addresses!![position].name + ", "+ addresses!![position].zip
        holder.itemView.address_type_text_view?.text = addresses!![position].default

        if (addresses!![position].address2!!.isNotEmpty()) {
            val addressLine1 = addresses!![position].address1 + ", "
            val addressLine2 = addresses!![position].address2
            holder.itemView.full_address_text_view?.text = addressLine1.plus(addressLine2)
        } else {
            holder.itemView.full_address_text_view?.text = addresses!![position].address1
        }
        if(addresses!![position].isSelected==false){
            holder.itemView.address_selection_radio_button.isChecked=false
        }else{
            holder.itemView.address_selection_radio_button.isChecked=true

        }

        holder.itemView.setOnClickListener {
         for(i in 0 until addresses!!.size){
             addresses!![i].isSelected=false
         }
                addresses!![position].isSelected=true
                addresses!![position].zip?.let { it1 ->
                    addresses!![position].id?.let { it2 ->
                        adapterListener.onAdapterItemClick(
                            it2,
                            it1,""
                        )
                    }
                }
notifyDataSetChanged()


        }
        //holder.bindView(adapterListener, addresses!![position])
    }

    fun updateList(addresses: ArrayList<AddAddressItems>?) {
        this.addresses = addresses
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(adapterListener: AddressAdapterListener, address: AddAddressItems) {


        }
    }

}
