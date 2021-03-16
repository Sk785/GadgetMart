package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.subcategory.AddressAdapterListener
import kotlinx.android.synthetic.main.my_addresses_activity_item.view.*

class MyAddressAdapter(
    val context: Context?,
    private var addressItems: ArrayList<AddAddressItems>?,
    private val adapterListener: AddressAdapterListener
) :
    RecyclerView.Adapter<MyAddressAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.my_addresses_activity_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (addressItems == null)
            0
        else
            addressItems.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(addressItems!![position], adapterListener, position)
    }

    fun updateList(addressItems: ArrayList<AddAddressItems>?) {
        this.addressItems = addressItems
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            addressItems: AddAddressItems?,
            adapterListener: AddressAdapterListener,
            position: Int
        ) {
            itemView.address_user_name.text = addressItems?.name
            itemView.user_phone_number_text.text = addressItems?.phone

//            if (addressItems?.default.equals("Yes")){
//
//            }

            if (addressItems?.address2 == null) {
                itemView.user_full_address_text.text = addressItems?.address1
            } else {
                if (addressItems.address2!!.isNotEmpty()) {
                    val addressLine1 = addressItems.address1 + ", "
                    val addressLine2 = addressItems.address2
                    itemView.user_full_address_text.text = addressLine1.plus(addressLine2)
                } else {
                    itemView.user_full_address_text.text = addressItems.address1
                }

            }

            itemView.icon_menu.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, itemView.icon_menu)
                popupMenu.menuInflater.inflate(R.menu.address_popup_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item: MenuItem ->

                    when (item.itemId) {

                        R.id.action_set_as_default -> {
                            adapterListener.onSetAsDefaultMenuItemSelected("" + addressItems?.id)
                        }

                        R.id.action_edit -> {
                            adapterListener.onAdapterItemTapped(
                                addressItems?.id,
                                "editAddress",
                                position
                            )
                        }

                        R.id.action_remove -> {
                            adapterListener.onAdapterItemTapped(
                                addressItems?.id,
                                "removeAddress",
                                position
                            )
                        }
                    }
                    true
                }
                popupMenu.show()
            }
            if(addressItems!!.address1!!.isEmpty()) {
                itemView.setOnClickListener {
                    adapterListener.onAdapterItemClick(
                        addressItems!!.id!!,
                        addressItems.address1!!,                        addressItems.zip!!

                    )
                }
            }else{
                itemView.setOnClickListener {
                    adapterListener.onAdapterItemClick(
                        addressItems!!.id!!,
                        addressItems.address1!!+", "+addressItems.address2!!, addressItems.zip!!
                    )
                }
            }
        }
    }

}
