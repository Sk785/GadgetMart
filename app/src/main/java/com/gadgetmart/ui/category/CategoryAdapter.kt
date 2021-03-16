package com.gadgetmart.ui.category

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.payumoney.core.utils.SdkHelper.getScreenWidth
import kotlinx.android.synthetic.main.dashboard_category_item.view.*


class CategoryAdapter(
    val context: Context?,
    private var categories: ArrayList<CategoryItem>?,
    private val adapterListener: DashboardAdapterListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.dashboard_category_item, parent, false)
        view.getLayoutParams().width = Resources.getSystem().displayMetrics.widthPixels / 5;
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (categories == null)
            0
        else
            categories!!.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(categories!![position], adapterListener)
    }

    fun updateList(categories: ArrayList<CategoryItem>?) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            categoryItem: CategoryItem?,
            adapterListener: DashboardAdapterListener
        ) {
            if (categoryItem!!.categoryImage != null && categoryItem.categoryImage != "") {
//                Picasso.get()
//                    .load(categoryItem.image)
//                    .placeholder(R.drawable.ic_logo_toolbar)
//                    .into(itemView.category_image_view)
                Glide.with(itemView.context)
                    .apply { RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.default_icon)
                        .override(256, 140)
                        .fitCenter(); }
                    .load(categoryItem.categoryImage)
                    .placeholder(R.drawable.default_icon)
                    .circleCrop()
                    .into(itemView.category_image_view)
            } else {
                Glide.with(itemView.context)
                    .load(R.drawable.default_icon)
                    .circleCrop()
                    .into(itemView.category_image_view)
            }
            itemView.category_name_text_view.text = categoryItem.categoryName

            Log.e("#####3r32454235##", categoryItem.categoryName);
            itemView.setOnClickListener {
                adapterListener.onAdapterItemCategoryTapped(categoryItem.categoryId , categoryItem.categoryName)
            }
        }
    }
}