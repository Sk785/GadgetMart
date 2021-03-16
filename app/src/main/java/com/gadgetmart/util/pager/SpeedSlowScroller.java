package com.gadgetmart.util.pager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class SpeedSlowScroller extends Scroller {

    private int mDuration = 1000;

    public SpeedSlowScroller(Context context) {
        super(context);
    }

    public SpeedSlowScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public SpeedSlowScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
