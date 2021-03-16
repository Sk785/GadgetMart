package com.gadgetmart.ui.product_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.ui.products_of_sub_category.ProductImages


class SlidingImageAdapter(var context: Context,var productsImagesListing : ArrayList<ProductImages>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (productsImagesListing == null || productsImagesListing.size == 0){
            0
        }else{
            productsImagesListing.size
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout: View =
            LayoutInflater.from(context).inflate(R.layout.image_frame_layout, container, false)!!
        val imageView: ImageView = imageLayout
            .findViewById<View>(R.id.image_frame) as ImageView

        val productImages = productsImagesListing[position]
        if (productImages.name != null && productImages.name != "") {
            Glide.with(context)
                .apply { RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_icon)
                    .override(256, 140)
                    .fitCenter(); }
                .load(productImages.name)
                .placeholder(R.drawable.default_icon)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.default_icon)
                .into(imageView)
        }

        container.addView(imageLayout, 0)

        return imageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}