package com.gadgetmart.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.gadgetmart.R;
import com.gadgetmart.ui.products.BannerProductCategory;
import com.gadgetmart.ui.products.ProductsActivity;

import java.util.ArrayList;

public class DemoInfiniteAdapter extends LoopingPagerAdapter<CategoryDeals> {

    public DemoInfiniteAdapter(Context context, ArrayList<CategoryDeals> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    //You should return the View (With data binded) to display in this method.
    @Override
    protected View getItemView(View convertView, int listPosition, ViewPager container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dashboard_image_frame_layout, null);
            container.addView(convertView);
        }
        ImageView imageview=convertView.findViewById(R.id.image_frame);


        if (itemList.get(listPosition).getName() != null && itemList.get(listPosition).getName() != "") {
            Glide.with(context)
                    .load(itemList.get(listPosition).getName())
                    .placeholder(R.drawable.default_icon)
                    .into(imageview);
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
                    .into(imageview);
        }
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemList.get(listPosition).getBanner_type()==1) {
                    ProductsActivity.Companion.start(context, itemList.get(listPosition).getDealsId(), "", true);
                }else{
                    BannerProductCategory.Companion.start(context, itemList.get(listPosition).getDealsId(), "");

                }
            }
        });

        return convertView;
    }
}
