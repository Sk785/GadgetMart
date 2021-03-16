package com.gadgetmart.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.gadgetmart.R
import com.gadgetmart.ui.products.ProductsActivity

class DashboardImageAdapter(
    var context: Context,
    var dealsImages: ArrayList<CategoryDeals>,
    var sectionName: String?
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (dealsImages == null || dealsImages.size == 0){
            0
        }else{
            dealsImages.size
        }
    }

    fun updateImages(dealsImages: ArrayList<CategoryDeals>){
        this.dealsImages = dealsImages
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout: View =
            LayoutInflater.from(context).inflate(R.layout.dashboard_image_frame_layout, container, false)!!
        val imageView: ImageView = imageLayout
            .findViewById<View>(R.id.image_frame) as ImageView
        val title: TextView = imageLayout
            .findViewById<View>(R.id.title_text) as TextView
        val subTitle: TextView = imageLayout
            .findViewById<View>(R.id.sub_title_text) as TextView

        val categoryDeals = dealsImages[position]
        if (categoryDeals.name != null && categoryDeals.name != "") {
            Glide.with(context)
                .load(categoryDeals.name)
                .placeholder(R.drawable.default_icon)
                .into(imageView)
//            Glide.with(context)
//                .apply { RequestOptions()
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.default_icon)
//                    .override(256, 140)
//                    .fitCenter(); }
//                .load(categoryDeals.dealsImage)
//                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.default_icon)
                .into(imageView)
        }
//        title.text = categoryDeals.name
//        subTitle.text = categoryDeals.sub_title
        imageLayout.setOnClickListener { categoryDeals.dealsId?.let { it1 ->
            sectionName?.let { it2 ->
                ProductsActivity.start(context,
                    it1, "", true
                )
            }
        } }

        container.addView(imageLayout, 0)

        return imageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        (container as ViewPager).removeView(`object` as View)
    }
}
