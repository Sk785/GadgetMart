package com.gadgetmart.util

import android.content.Context
import androidx.annotation.StringRes

// TODO singleton by di
class ResourceProvider(
    private val context: Context
) {

    fun stringRes(@StringRes stringRes: Int) = context.getString(stringRes)
    fun stringRes(@StringRes stringRes: Int, vararg formatArgs: Any) =
        context.getString(stringRes, *formatArgs)
}