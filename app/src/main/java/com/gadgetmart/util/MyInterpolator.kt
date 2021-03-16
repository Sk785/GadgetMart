package com.gadgetmart.util

class MyInterpolator(var mAmplitude : Double , var mFrequency : Double) : android.view.animation.Interpolator {

    override fun getInterpolation(time: Float): Float {
        var float = (-1 * Math.pow(Math.E, -time/ mAmplitude) * Math.cos(mFrequency * time) + 1).toFloat()
        return float
    }
}