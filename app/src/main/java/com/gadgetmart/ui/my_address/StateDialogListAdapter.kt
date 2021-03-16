package com.gadgetmart.ui.my_address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import kotlinx.android.synthetic.main.dialog_list_layout_item.view.*

class StateDialogListAdapter(
    val context: Context?,
    var states: ArrayList<State>?,
    private val adapterListener: DialogAdapterListener
) :
    RecyclerView.Adapter<StateDialogListAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.dialog_list_layout_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (states == null)
            0
        else
            states.let { it!!.size }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(states!![position], adapterListener)
    }

    fun updateList(states: ArrayList<State>?) {
        this.states = states
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            state: State?,
            adapterListener: DialogAdapterListener
        ) {
            itemView.item_name_text_view?.text = (state?.name)

            itemView.setOnClickListener {

                itemView.radio_button?.isChecked = !(itemView.radio_button)?.isChecked!!
                state?.name?.let { it1 ->
                    state?.id?.let { it2 ->
                        adapterListener.onAdapterItemClick(
                            "state",
                            it2,
                            it1
                        )
                    }
                }
            }
        }
    }
}
