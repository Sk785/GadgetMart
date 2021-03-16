package com.gadgetmart.util.pager;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class CrossfadePageTransformer implements ViewPager.PageTransformer{
    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-position*page.getWidth());

        page.setAlpha(1- Math.abs(position));


    }
}