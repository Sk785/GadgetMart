package com.gadgetmart.ui.products_of_sub_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.category.ProductVariation
import kotlinx.android.synthetic.main.variation_item.view.*

class VariationAdapter(
    val context: Context?,
    private var variations: ArrayList<ProductVariation>?,
    private var dashboardAdapterListener: ProductsAdapterListener
) :
    RecyclerView.Adapter<VariationAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.variation_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variations?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(dashboardAdapterListener, variations!![position], position)
    }

    fun updateList(variations: ArrayList<ProductVariation>?) {
        this.variations = variations
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            dashboardAdapterListener: ProductsAdapterListener,
            variation: ProductVariation,
            position: Int
        ) {

            if (variation.isSelected) {
                itemView.variation_image_view.setBackgroundResource(R.drawable.shape_rectangle_hollow_inside_red_border)
                itemView.variation_name_text_view.setTextColor(itemView.context.resources.getColor(R.color.colorPrimary))
            } else {
                itemView.variation_image_view.setBackgroundResource(R.drawable.shape_rectangle_hollow_inside_grey_border)
                itemView.variation_name_text_view.setTextColor(itemView.context.resources.getColor(R.color.colorBlackPure))
            }
            Glide.with(itemView.context)
                .apply { RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_icon)
                    .override(256, 140)
                    .fitCenter(); }
                .load(variation.productImages?.get(0)?.name)
                .placeholder(R.drawable.default_icon)
                .into(itemView.variation_image_view)

            itemView.variation_name_text_view?.text = variation.variationValue

            itemView.setOnClickListener {
                variation.isSelected = true
                itemView.variation_image_view.setBackgroundResource(R.drawable.shape_rectangle_hollow_inside_red_border)
                itemView.variation_name_text_view.setTextColor(
                    itemView.context.resources.getColor(R.color.colorPrimary)
                )
                itemView.variation_image_view.setBackgroundResource(R.drawable.shape_rectangle_hollow_inside_grey_border)
                itemView.variation_name_text_view.setTextColor(
                    itemView.context.resources.getColor(R.color.colorBlackPure)
                )
                dashboardAdapterListener.onAdapterItemTapped(
                    variation.variationId,
                    variation.productId,
                    adapterPosition,""
                )
            }
        }
    }

}
