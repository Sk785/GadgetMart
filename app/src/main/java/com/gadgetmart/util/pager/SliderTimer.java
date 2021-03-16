package com.gadgetmart.util.pager;

import android.app.Activity;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import com.gadgetmart.app.Global;

import java.util.TimerTask;


public class SliderTimer extends TimerTask {
    private ViewPager viewPager;
    private int size;
    private Activity activity;
    int dataSize=0;
    int updateSizde=0;
    Global global;

    public SliderTimer(ViewPager viewPager, int size, Activity activity) {
        this.viewPager = viewPager;
        this.size = size;
        this.activity = activity;
        updateSizde=size;
        global=(Global)activity.getApplicationContext();
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (global.isResume() == true) {
                    if (dataSize == size - 1) {
                        if (updateSizde == 1) {

                            viewPager.setCurrentItem(0, true);
                            dataSize = 0;
                            updateSizde = 0;

                        } else {
                            updateSizde = updateSizde -1;

                            viewPager.setCurrentItem(updateSizde, true);
                            Log.e("updateSizde", "" + updateSizde);

                        }


                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        if (viewPager.getCurrentItem() == size - 1) {

                            dataSize = size - 1;
                            updateSizde = updateSizde-1;
                            Log.e("updateSizde", "" + updateSizde + " " + dataSize);

                        }
                    }
//                if (viewPager.getCurrentItem() < size - 1) {
//                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
//                    if(viewPager.getCurrentItem()==size-1){
//                        dataSize=size;
//                    }
//                } else{
//                    viewPager.setCurrentItem(0, true);
//
//                }
                }
            }
        });
    }



}
